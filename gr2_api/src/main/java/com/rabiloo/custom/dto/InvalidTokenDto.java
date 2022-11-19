package com.rabiloo.custom.dto;

import java.util.Date;

public class InvalidTokenDto {
    private Date timeStamp;
    private int status;
    private String error;
    private String message;

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InvalidTokenDto() {
        this.timeStamp = new Date();
        this.status = 900;
        this.error = "Token is invalid!";
        this.message = "Token is wrong or token is expired!!!";
    }

    @Override
    public String toString() {
        return "InvalidTokenDto{" +
                "timeStamp=" + timeStamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
