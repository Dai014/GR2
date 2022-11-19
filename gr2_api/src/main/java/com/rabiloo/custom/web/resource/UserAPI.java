package com.rabiloo.custom.web.resource;

import com.rabiloo.custom.dto.request.SaveUserDto;
import com.rabiloo.custom.dto.response_api.ResponseCase;
import com.rabiloo.custom.dto.response_api.ServerResponseDto;
import com.rabiloo.custom.entity.UserEntity;
import com.rabiloo.custom.enums.WebUserRole;
import com.rabiloo.custom.service.web_user.UserService;
import com.rabiloo.custom.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.rabiloo.custom.utils.Constants.BASE_API_URL_WEB;

@RestController
@RequestMapping(BASE_API_URL_WEB + "user/")
public class UserAPI {

    @Autowired
    private UserService userService;

    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @GetMapping("/detail")
    public ResponseEntity<ServerResponseDto> detailFacilityUser(@RequestParam Long userId) {
        return ResponseEntity.ok(new ServerResponseDto(ResponseCase.SUCCESS, userService.getDetailUser(userId)));
    }

    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @PostMapping("/delete")
    public ResponseEntity<ServerResponseDto> deleteFacilityUser(@RequestParam Long userId) {
        boolean isDeleteSuccess = userService.delete(userId);
        return ResponseEntity.ok(new ServerResponseDto(isDeleteSuccess ? ResponseCase.SUCCESS : ResponseCase.ERROR));
    }

    @PreAuthorize("@authorizationService.isSystemAdmin() or @authorizationService.isUserRole()")
    @PostMapping("/resetPassword")
    public ResponseEntity<ServerResponseDto> resetPassword(@RequestParam Long userId,
                                                           @RequestParam String newPassword) {
        boolean isResetSuccess = userService.resetPassword(userId, newPassword);
        return ResponseEntity.ok(new ServerResponseDto(isResetSuccess ? ResponseCase.SUCCESS : ResponseCase.ERROR));
    }

    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @PostMapping("/checkUsernameExist")
    public ResponseEntity<Boolean> checkUsernameExist(@RequestParam String username) {
        boolean isUsernameExist = userService.isUsernameExist(username);
        return ResponseEntity.ok(isUsernameExist);
    }

    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @GetMapping("/listUser")
    public ResponseEntity<Page<UserEntity>> pageAllUser(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "20") int size,
                                                                @RequestParam(defaultValue = "createdTime") String sortField,
                                                                @RequestParam(defaultValue = "desc") String sortDir,
                                                                @RequestParam(required = false) String keyword) {
        Pageable pageable = PageableUtils.from(sortDir, sortField, page, size);
        Page<UserEntity> pageAllUser = userService.findUser(keyword, pageable);
        return ResponseEntity.ok(pageAllUser);
    }

    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @PostMapping("/deleteMultiple")
    public ResponseEntity<ServerResponseDto> deleteMultiple(@RequestParam List<Long> userIds) {
        boolean isDeleteSuccess = userService.deleteMultiple(userIds);
        return ResponseEntity.ok(new ServerResponseDto(isDeleteSuccess ? ResponseCase.SUCCESS : ResponseCase.ERROR));
    }

    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @GetMapping("/changeActiveStatus")
    public ResponseEntity<ServerResponseDto> changeStatus(@RequestParam String userId) {
        boolean status = userService.changeStatusUser(Long.valueOf(userId));
        return ResponseEntity.ok(new ServerResponseDto(status ? ResponseCase.SUCCESS : ResponseCase.ERROR));
    }

    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @GetMapping("/generatePassword")
    public ResponseEntity<String> generateDefaultPassword() {
        String defaultPassword = userService.generatePassword();
        return ResponseEntity.ok(defaultPassword);
    }

    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @PostMapping("/saveUser")
    public ResponseEntity<ServerResponseDto> saveUser(@RequestBody SaveUserDto saveUserDto) {
        boolean isSaveSuccess = userService.saveUser(saveUserDto);
        return isSaveSuccess ? ResponseEntity.ok(new ServerResponseDto(ResponseCase.SUCCESS))
                : ResponseEntity.ok(new ServerResponseDto(ResponseCase.ERROR));
    }
}
