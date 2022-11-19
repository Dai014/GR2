package com.rabiloo.custom.web.login_web;

import com.rabiloo.custom.dto.response_api.ServerResponseDto;
import com.rabiloo.custom.security.CustomUserDetails;
import com.rabiloo.custom.service.web_user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.rabiloo.custom.utils.Constants.BASE_API_URL_WEB;

@RestController
@RequestMapping(BASE_API_URL_WEB)
public class ChangePassword_WebAPI {

    @Autowired
    private UserService userService;

    @PostMapping("/changePasswordFirst")
    public ResponseEntity<ServerResponseDto> changePasswordFirst(@RequestParam String newPassword,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        ServerResponseDto response = userService.changePasswordFirst(newPassword, userDetails.getUserId());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<ServerResponseDto> changePassword(@RequestParam String oldPassword,
                                                            @RequestParam String newPassword,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ServerResponseDto response = userService.changePassword(oldPassword, newPassword, userDetails.getUserId());
        return ResponseEntity.ok().body(response);
    }

}
