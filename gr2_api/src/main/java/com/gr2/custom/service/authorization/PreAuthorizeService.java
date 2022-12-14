package com.gr2.custom.service.authorization;

import com.gr2.base.security.CurrentUserDetailsContainer;
import com.gr2.custom.enums.WebUserRole;
import com.gr2.custom.enums.enum_helper.EnumUtils;
import com.gr2.custom.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "authorizationService")
public class PreAuthorizeService {

    @Autowired
    private CurrentUserDetailsContainer currentUserDetailsContainer;

    public boolean isSystemAdmin() {
        CustomUserDetails userDetails = currentUserDetailsContainer.getUserDetails();
        return checkRolePermission(userDetails, WebUserRole.SYSTEM_ADMIN);
    }

    public boolean isUserRole() {
        CustomUserDetails userDetails = currentUserDetailsContainer.getUserDetails();
        return checkRolePermission(userDetails, WebUserRole.USER_ROLE);
    }

    private boolean checkRolePermission(CustomUserDetails userDetails, WebUserRole roleCanAccessResources) {
        if (userDetails == null) {
            return false;
        }
        String roleStrOfCurrentUser = userDetails.getRole();
        if (roleStrOfCurrentUser == null) {
            return false;
        }
        WebUserRole roleEnumFromCurrentUser = EnumUtils.fromString(roleStrOfCurrentUser, WebUserRole.class);
        if (roleEnumFromCurrentUser == null) {
            return false;
        }

        return roleCanAccessResources == roleEnumFromCurrentUser
                || roleCanAccessResources.getOrderPermission() > roleEnumFromCurrentUser.getOrderPermission();
    }

}
