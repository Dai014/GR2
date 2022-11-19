package com.rabiloo.custom.entity.token;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
public class TokenEntity {
    private Long id;
    private String token;
    private TokenType tokenType;
    private TypeUserToken typeUserToken;
    private Long userId;
    private Long deviceId;
    private Boolean isUsed;
    private Boolean isDeleted;
    private Date createdTime;

    public TokenEntity(String token) {
        this.token = token;
    }

    public TokenEntity(String token, TokenType tokenType, TypeUserToken typeUserToken, Long userId, Long deviceId) {
        this.token = token;
        this.tokenType = tokenType;
        this.typeUserToken = typeUserToken;
        this.userId = userId;
        this.deviceId = deviceId;
        this.isUsed = false;
        this.isDeleted = false;
        this.createdTime = new Date();
    }
}
