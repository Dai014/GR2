package com.gr2.custom.dto.response_api;


/**
 * ResponseStatus
 * return when calling api
 * @author dainv
 */
public class ResponseStatus {
    public int code;
    public String message;

    public ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
