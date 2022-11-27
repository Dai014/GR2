package com.gr2.custom.security;

import com.gr2.custom.entity.UserEntity;
import com.gr2.custom.service.web_user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    //login for swagger
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("aaa: " + authentication.getName());
        String username = authentication.getName();
        LOGGER.info("validate user: {}", username);
        String password = authentication.getCredentials().toString();
        UserEntity userEntity = userService.validateSystemUser(username, password);
        if (userEntity.getId() != null) {
            Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"));
            UserDetails userDetails = new CustomUserDetails("", authorities, userEntity.getId(),
                    userEntity.getUsername(),  "ROLE_SYSTEM_ADMIN");
            return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        } else {
            throw new BadCredentialsException("Username or password is incorrect!");
        }
    }

    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
