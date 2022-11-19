package com.rabiloo.custom.utils;

public class Constants {

    public static final String EMPTY_STRING = "";

    public static final int HTTP_CODE_FOR_INVALID_TOKEN = 900;
    public static final int HTTP_CODE_MUST_CHANGE_PASSWORD = 800;

    public static final String BASE_API_URL_WEB = "api/v1/web/";

    public static final String PASSWORD_PATTERN = "^[^ ]*$";
    public static final String USERNAME_PATTERN = "^[a-z0-9\\._@]*$";
    public static final String EMAIL_PATTERN = "^(.+)@(\\S+)$";
    public static final String ONLY_NUMBER_PATTERN = "^\\d+$";

    public static final String DATE_PATTERN = "YYYY-MM-dd";

    public static final String PARAM_ACCESS_TOKEN_OF_APP = "accessToken";
    public static final String PARAM_ACCESS_TOKEN_OF_WEB = "accessTokenWeb";

    public static final String CHANGE_PASSWORD_FIRST = BASE_API_URL_WEB + "changePasswordFirst";
    public static final String LOGOUT = BASE_API_URL_WEB + "logout";
    public static final String LOGIN = BASE_API_URL_WEB + "login";
    public static final String CHECK_CODE_FOR_LOGIN = BASE_API_URL_WEB + "checkCodesForLogin";

}
