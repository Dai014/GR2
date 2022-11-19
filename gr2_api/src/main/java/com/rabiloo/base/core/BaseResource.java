package com.rabiloo.base.core;

import com.rabiloo.base.security.CurrentUserDetailsContainer;
import com.rabiloo.custom.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseResource<S extends BaseService> {

    @Autowired
    protected S service;

    @Autowired
    private CurrentUserDetailsContainer userDetailsContainer;

    public CustomUserDetails getCurrentUser() {
        return this.userDetailsContainer.getUserDetails();
    }

}
