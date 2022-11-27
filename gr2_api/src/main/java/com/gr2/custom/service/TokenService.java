package com.gr2.custom.service;

import com.gr2.custom.entity.token.TokenEntity;
import com.gr2.custom.entity.token.TokenType;
import com.gr2.custom.entity.token.TypeUserToken;
import com.gr2.custom.entity.UserEntity;
import com.gr2.custom.service.cache.TokenCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private TokenCacheService tokenCacheService;

    private String generateToken() {
        String tokenResult = "";
        while ("".equals(tokenResult) || isExist(tokenResult)) {
            String token = UUID.randomUUID().toString();
            String generateToken = generateToken(token);
            String timeNow = String.valueOf(System.currentTimeMillis());
            tokenResult = generateToken + timeNow;
        }
        return tokenResult;
    }

    private String generateToken(String token) {
        final TokenEntity myToken = new TokenEntity(token);
        return token + myToken.hashCode();
    }

    private boolean isExist(String accessToken) {
        return tokenCacheService.getTokenFromCache(accessToken) != null;
    }

    public String createForWebUser(UserEntity userEntity) {
        String accessToken = generateToken();
        TokenEntity accessTokenEntity = new TokenEntity(accessToken, TokenType.ACCESS_TOKEN, TypeUserToken.USER, userEntity.getId(), null);
        tokenCacheService.updateCacheToken(accessTokenEntity);
        LOGGER.info("generate token for userId: {} successfully!!", userEntity.getId());

        return accessToken;
    }
}
