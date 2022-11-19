package com.rabiloo.base.security;

import com.rabiloo.custom.security.AuthenticationUtils;
import com.rabiloo.custom.security.CustomUserDetails;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 *  class hold current login user detail instance.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentUserDetailsContainer implements Serializable {

    //    number?
    private static final long serialVersionUID = 1L;

    private CustomUserDetails userDetails;

    @PostConstruct
    public void init() {
        if (!AuthenticationUtils.isAuthenticated()) {
            this.userDetails = null;
            return;
        }
        this.userDetails = (CustomUserDetails) AuthenticationUtils.getAuthentication().getPrincipal();
    }

    public CustomUserDetails getUserDetails() {
        return userDetails;
    }
}
