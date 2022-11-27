package com.gr2.custom.service.cache;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gr2.custom.entity.token.TokenEntity;
import com.gr2.custom.entity.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCacheService.class);

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public TokenEntity getTokenFromCache(String token) {
        TokenEntity tokenEntity = null;
        Object json = redisTemplate.opsForValue().get(token);
        if (json != null) {
            try {
                tokenEntity = new Gson().fromJson(String.valueOf(json), TokenEntity.class);
                if (tokenEntity != null) {
                    LOGGER.info("Get token from cache success, token is: {}", tokenEntity.getToken());
                }
            } catch (JsonSyntaxException e) {
                LOGGER.error("Json is wrong format!. Json is: {}", json);
            }
        }
        return tokenEntity;
    }

    public void updateCacheToken(TokenEntity token) {
        LOGGER.info("Update token cache");
        Gson gson = new Gson();
        if (token.getTokenType().equals(TokenType.ACCESS_TOKEN)) {
            redisTemplate.opsForValue().set(token.getToken(), gson.toJson(token), 1, TimeUnit.DAYS);
        } else {
            redisTemplate.opsForValue().set(token.getToken(), gson.toJson(token), 30, TimeUnit.DAYS);
        }
    }
}
