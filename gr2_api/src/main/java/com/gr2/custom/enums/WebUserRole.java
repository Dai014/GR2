package com.gr2.custom.enums;


import com.gr2.custom.enums.enum_helper.AbstractEnumConverter;
import com.gr2.custom.enums.enum_helper.ValueEnumInterface;

public enum WebUserRole implements ValueEnumInterface<String> {

    SYSTEM_ADMIN("ROLE_SYSTEM_ADMIN", 1),
    USER_ROLE("USER_ROLE", 2);

    private final String value;
    private final int orderPermission;

    WebUserRole(String value, int orderPermission) {
        this.value = value;
        this.orderPermission = orderPermission;
    }

    public int getOrderPermission() {
        return orderPermission;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static class Converter extends AbstractEnumConverter<WebUserRole, String> {
        public Converter() {
            super(WebUserRole.class);
        }
    }
}

