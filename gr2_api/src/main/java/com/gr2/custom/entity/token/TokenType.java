package com.gr2.custom.entity.token;

public enum TokenType {

    ACCESS_TOKEN(1), REFRESH_TOKEN(2),REGISTER_TOKEN(3), FORGOT_PASSWORD_TOKEN(4);

    int value;

    TokenType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}
