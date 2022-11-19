package com.rabiloo.custom.dto.response_api;

import com.rabiloo.custom.enums.WebUserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginWebResponseDto {

    private String name;
    private String accessToken;
    private String userName;
    private WebUserRole role;
    private boolean isFirstLogin;
    private Long userId;

    public LoginWebResponseDto(String accessToken, String userName,
                               String name, WebUserRole role, boolean isFirstLogin,
                               Long userId) {
        this.accessToken = accessToken;
        this.userName = userName;
        this.role = role;
        this.name = name;
        this.isFirstLogin = isFirstLogin;
        this.userId = userId;
    }
}
