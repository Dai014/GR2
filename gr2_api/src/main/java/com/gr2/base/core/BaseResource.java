package com.gr2.base.core;

import com.gr2.base.security.CurrentUserDetailsContainer;
import com.gr2.custom.security.CustomUserDetails;
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
