package com.gr2.custom.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gr2.custom.dto.InvalidTokenDto;
import com.gr2.custom.entity.UserEntity;
import com.gr2.custom.entity.token.TokenEntity;
import com.gr2.custom.entity.token.TypeUserToken;
import com.gr2.custom.service.cache.TokenCacheService;
import com.gr2.custom.service.web_user.UserService;
import com.gr2.custom.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.gr2.custom.utils.Constants.HTTP_CODE_FOR_INVALID_TOKEN;

@Component
@Order(1)
public class CustomAccessTokenAuthenticationProvider implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessTokenAuthenticationProvider.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TokenCacheService tokenService;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (AuthenticationUtils.isAuthenticated()) {
            chain.doFilter(request, response);
            return;
        }

        String webAccessToken = httpServletRequest.getHeader(Constants.PARAM_ACCESS_TOKEN_OF_WEB);
        if (webAccessToken == null) {
            chain.doFilter(request, response);
            return;
        }
        boolean isAuthenticateWebUserSuccess = authenticateForWebUser(webAccessToken);
        if (!isAuthenticateWebUserSuccess) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean authenticateForWebUser(String webAccessToken) {
        LOGGER.info("Getting TokenEntity from cache...");
        TokenEntity tokenEntity = tokenService.getTokenFromCache(webAccessToken);
        if (tokenEntity == null) {
            return false;
        }

        //todo: get user's cache here
        if (tokenEntity.getTypeUserToken() == TypeUserToken.USER) {
            LOGGER.info("Getting UserEntity from cache...");
            UserEntity userEntity = userService.findById(tokenEntity.getUserId());
            if (userEntity == null) {
                return false;
            }
            setWebUserAuthenticatedManually(userEntity);
        }
        return true;
    }

    private void setWebUserAuthenticatedManually(UserEntity user) {
        LOGGER.info("Setting user authenticated manually for web user : {}", user.getUsername());
        UserDetails userDetails = CustomUserDetails.createForWebUser(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void createResponseForInvalidToken(HttpServletResponse httpServletResponse) throws IOException {
        LOGGER.info("Invalid Token!!!");
        InvalidTokenDto invalidTokenDto = new InvalidTokenDto();
        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.getWriter().write(mapper.writeValueAsString(invalidTokenDto));
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
        httpServletResponse.setStatus(HTTP_CODE_FOR_INVALID_TOKEN);
    }

}
