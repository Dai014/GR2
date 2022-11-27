package com.gr2.custom.filter;

import com.gr2.base.security.CurrentUserDetailsContainer;
import com.gr2.custom.entity.UserEntity;
import com.gr2.custom.security.AuthenticationUtils;
import com.gr2.custom.security.CustomUserDetails;
import com.gr2.custom.service.web_user.UserService;
import com.gr2.custom.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.gr2.custom.utils.Constants.BASE_API_URL_WEB;
import static com.gr2.custom.utils.Constants.HTTP_CODE_MUST_CHANGE_PASSWORD;

@Component
@Order(2)
public class ChangePasswordFilter implements Filter {
    @Autowired
    private UserService userService;

    @Autowired
    private CurrentUserDetailsContainer currentUserDetailsContainer;


    @Override
    public void destroy() {
    }

    /**
     * This filter will check for request have prefix api/v1/web
     * 1. if user is not login and request is checkCodesForLogin or login -> allow
     * 2. else: with another request if user is not login -> not allow
     * 3. else: if user is login, but is not change password first -> return code that user need to login
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestUri = httpServletRequest.getRequestURI();

        if (!requestUri.startsWith(BASE_API_URL_WEB)) {
            chain.doFilter(request, response);
            return;
        }

        if (!AuthenticationUtils.isAuthenticated()) {
            chain.doFilter(request, response);
            return;
        }

        CustomUserDetails userDetails = currentUserDetailsContainer.getUserDetails();
        if (userDetails == null) {
            return;
        }

        UserEntity userEntity = userService.findByIdAndIsDeletedFalse(userDetails.getUserId());
        if (userEntity != null && userEntity.isChangePassword()) {
            chain.doFilter(request, response);
            return;
        }

        if (Constants.CHANGE_PASSWORD_FIRST.equals(requestUri)) {
            chain.doFilter(request, response);
        }

        httpServletResponse.setStatus(HTTP_CODE_MUST_CHANGE_PASSWORD);
    }

    @Override
    public void init(FilterConfig arg0) {
    }

}
