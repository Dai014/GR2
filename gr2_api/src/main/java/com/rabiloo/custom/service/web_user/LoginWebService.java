package com.rabiloo.custom.service.web_user;

import com.rabiloo.custom.dto.request.LoginWebRequestDto;
import com.rabiloo.custom.dto.response_api.LoginWebResponseDto;
import com.rabiloo.custom.dto.response_api.ResponseCase;
import com.rabiloo.custom.dto.response_api.ServerResponseDto;
import com.rabiloo.custom.entity.UserEntity;
import com.rabiloo.custom.enums.WebUserRole;
import com.rabiloo.custom.security.CustomUserDetails;
import com.rabiloo.custom.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class LoginWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginWebService.class);

    @Autowired
    private TokenService accessTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public UserEntity getUserByLoginDto(LoginWebRequestDto loginWebDto) {
        String userName = loginWebDto.getUserName();

        UserEntity userEntity = userService.findByUsernameAndRole(userName, WebUserRole.SYSTEM_ADMIN);
        if (userEntity == null) {
            LOGGER.info("Can not find user with userName: {} ", userName);
            return null;
        }

        return userEntity;
    }

    public ServerResponseDto loginWeb(LoginWebRequestDto loginWebDto) {
        String userName = loginWebDto.getUserName();

        UserEntity user = getUserByLoginDto(loginWebDto);
        if (user == null) {
            LOGGER.info("Can not find user with userName: {} ", userName);
            return new ServerResponseDto(ResponseCase.INVALID_WEB_LOGIN_PARAM);
        }
        if (!passwordEncoder.matches(loginWebDto.getPassword(), user.getPassword())) {
            LOGGER.info("Invalid password with userName: {}", userName);
            return new ServerResponseDto(ResponseCase.INVALID_WEB_PASSWORD);
        }

        String accessToken = accessTokenService.createForWebUser(user);
        LOGGER.info("Authenticate user with name: {} successfully!!", userName);
        setAuthenticateManually(user);

        LoginWebResponseDto loginWebResponseDto = new LoginWebResponseDto(accessToken, userName,
                user.getName(), user.getRole(), user.isFirstLogin(), user.getId());

        return new ServerResponseDto(ResponseCase.SUCCESS, loginWebResponseDto);
    }

    private void setAuthenticateManually(UserEntity user) {
        CustomUserDetails customUserDetails =
                CustomUserDetails.createForWebUser(user);
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        SecurityContextHolder
                .getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(customUserDetails, "", authorities));
    }

}
