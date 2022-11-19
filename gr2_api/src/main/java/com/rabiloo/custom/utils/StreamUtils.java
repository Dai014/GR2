package com.rabiloo.custom.utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamUtils {

    /**
     * Like Collectors.groupingBy, but accepts null keys.
     */
    private static <V, K> Collector<V, ?, Map<K, List<V>>> groupingBy_WithNullKey(
            Function<? super V, ? extends K> classifier
    ) {
        return Collectors.toMap(
                classifier,
                Collections::singletonList,
                (List<V> oldList, List<V> newEl) -> {
                    List<V> newList = new ArrayList<>(oldList.size() + 1);
                    newList.addAll(oldList);
                    newList.addAll(newEl);
                    return newList;
                });
    }

    public static <V, K> Map<K, List<V>> groupingToHashMapWithNullKey(
            Collection<V> elements,
            Function<? super V, ? extends K> classifier
    ) {
        return elements
                .stream()
                .collect(StreamUtils.groupingBy_WithNullKey(classifier));
    }

    public static <V, K> Map<K, List<V>> groupingToHashMap(
            Collection<V> elements,
            Function<? super V, ? extends K> classifier
    ) {
        return elements
                .stream()
                .collect(Collectors.groupingBy(classifier));
    }

    public static <V, K> Map<K, List<V>> groupingToLinkedHashMap(
            Collection<V> elements,
            Function<? super V, ? extends K> classifier
    ) {
        return elements
                .stream()
                .collect(Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.toList()));
    }


    public static <E, K, V> Map<K, E> toHashMapWithNoDuplicateKey(Collection<V> elements,
                                                                  Function<V, K> funcGetKey,
                                                                  Function<V, E> funcGetValue) {
        return elements.stream().collect(Collectors.toMap(funcGetKey, funcGetValue));
    }

    public static <E, K, V> Map<K, V> mapByProp(Collection<V> elements,
                                                Function<V, K> funcGetKey) {
        return elements.stream().collect(Collectors.toMap(funcGetKey, v -> v));
    }
}
