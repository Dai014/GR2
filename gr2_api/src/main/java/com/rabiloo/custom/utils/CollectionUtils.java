package com.rabiloo.custom.utils;

import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    public static <E> E getLastEntry(List<E> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    public static boolean isNotNullAndNotEmpty(Collection<?> list) {
        return list != null && !list.isEmpty();
    }
}
