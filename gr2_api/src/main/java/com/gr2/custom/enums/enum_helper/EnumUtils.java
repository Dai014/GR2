package com.gr2.custom.enums.enum_helper;

import com.gr2.custom.utils.Constants;

public class EnumUtils {

    public static <E extends Enum<?> & ValueEnumInterface<?>> E fromString(String str, Class<E> c) {
        String strAfterTrim = str.trim();
        for (E e : c.getEnumConstants()) {
            if (e.getValue().toString().equalsIgnoreCase(strAfterTrim)) {
                return e;
            }
        }
        return null;
    }

    public static <E extends Enum<?> & ValueEnumInterface<?>> String fromValue(E e) {
        if (e == null) {
            return Constants.EMPTY_STRING;
        }
        return e.getValue().toString();
    }
}
