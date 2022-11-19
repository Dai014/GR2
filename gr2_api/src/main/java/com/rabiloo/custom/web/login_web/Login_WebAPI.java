package com.rabiloo.custom.web.login_web;

import com.rabiloo.custom.dto.request.LoginWebRequestDto;
import com.rabiloo.custom.dto.response_api.ResponseCase;
import com.rabiloo.custom.dto.response_api.ServerResponseDto;
import com.rabiloo.custom.entity.UserEntity;
import com.rabiloo.custom.security.CustomUserDetails;
import com.rabiloo.custom.service.web_user.LoginWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.rabiloo.custom.utils.Constants.BASE_API_URL_WEB;

@RestController
@RequestMapping(BASE_API_URL_WEB)
public class Login_WebAPI {

    @Autowired
    private LoginWebService loginWebService;

    @PostMapping("/checkCodesForLogin")
    public ResponseEntity<ServerResponseDto> checkCodesForLogin(@RequestBody LoginWebRequestDto loginWebDto) {
        UserEntity userEntity = loginWebService.getUserByLoginDto(loginWebDto);
        if (userEntity == null) {
            return ResponseEntity.ok(new ServerResponseDto(ResponseCase.INVALID_WEB_LOGIN_PARAM));
        } else {
            return ResponseEntity.ok(new ServerResponseDto(ResponseCase.SUCCESS, loginWebDto));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ServerResponseDto> login(@RequestBody LoginWebRequestDto loginWebDto) {
        ServerResponseDto serverResponseDto = loginWebService.loginWeb(loginWebDto);
        return ResponseEntity.ok(serverResponseDto);
    }

    @GetMapping("/isAuthenticatedOnServer")
    public ResponseEntity<Boolean> isAuthenticated(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userDetails != null);
    }

}
