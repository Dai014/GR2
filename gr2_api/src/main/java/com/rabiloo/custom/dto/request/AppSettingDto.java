package com.rabiloo.custom.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppSettingDto {
    private Long id;
    private String nameApp;
    private String elinkCode;
    private int contractCode;
    private String term;
    private Date createdTimeContract;

    public AppSettingDto() {
    }
}
