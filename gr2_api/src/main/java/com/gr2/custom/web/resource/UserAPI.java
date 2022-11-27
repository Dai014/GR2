package com.gr2.custom.web.resource;

import com.gr2.custom.dto.request.SaveUserDto;
import com.gr2.custom.dto.response_api.ResponseCase;
import com.gr2.custom.dto.response_api.ServerResponseDto;
import com.gr2.custom.entity.UserEntity;
import com.gr2.custom.service.web_user.UserService;
import com.gr2.custom.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gr2.custom.utils.Constants.BASE_API_URL_WEB;

/**
 * <h1>API crud USER </h1>
 */
@RestController
@RequestMapping(BASE_API_URL_WEB + "user/")
public class UserAPI {

    @Autowired
    private UserService userService;

    /**
     * <h3>api detail user</h3>
     * @param userId
     * @return
     */
    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @GetMapping("/detail")
    public ResponseEntity<ServerResponseDto> detailFacilityUser(@RequestParam Long userId) {
        return ResponseEntity.ok(new ServerResponseDto(ResponseCase.SUCCESS, userService.getDetailUser(userId)));
    }

    /**
     * <h3>api delete user</h3>
     * @param userId
     * @return
     */
    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @PostMapping("/delete")
    public ResponseEntity<ServerResponseDto> deleteFacilityUser(@RequestParam Long userId) {
        boolean isDeleteSuccess = userService.delete(userId);
        return ResponseEntity.ok(new ServerResponseDto(isDeleteSuccess ? ResponseCase.SUCCESS : ResponseCase.ERROR));
    }

    /**
     * <h3>api reset password</h3>
     * @param userId
     * @param newPassword
     * @return
     */
    @PreAuthorize("@authorizationService.isSystemAdmin() or @authorizationService.isUserRole()")
    @PostMapping("/resetPassword")
    public ResponseEntity<ServerResponseDto> resetPassword(@RequestParam Long userId,
                                                           @RequestParam String newPassword) {
        boolean isResetSuccess = userService.resetPassword(userId, newPassword);
        return ResponseEntity.ok(new ServerResponseDto(isResetSuccess ? ResponseCase.SUCCESS : ResponseCase.ERROR));
    }

    /**
     * <h3>api list user</h3>
     * @param page
     * @param size
     * @param sortField
     * @param sortDir
     * @param keyword
     * @return
     */
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

    /**
     * <h3>api delete multiple user</h3>
     * @param userIds
     * @return
     */
    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @PostMapping("/deleteMultiple")
    public ResponseEntity<ServerResponseDto> deleteMultiple(@RequestParam List<Long> userIds) {
        boolean isDeleteSuccess = userService.deleteMultiple(userIds);
        return ResponseEntity.ok(new ServerResponseDto(isDeleteSuccess ? ResponseCase.SUCCESS : ResponseCase.ERROR));
    }

    /**
     * <h3>api  changeStatus</h3>
     * @param userId
     * @return
     */
    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @GetMapping("/changeActiveStatus")
    public ResponseEntity<ServerResponseDto> changeStatus(@RequestParam String userId) {
        boolean status = userService.changeStatusUser(Long.valueOf(userId));
        return ResponseEntity.ok(new ServerResponseDto(status ? ResponseCase.SUCCESS : ResponseCase.ERROR));
    }

    /**
     * <h3>api  save user</h3>
     * @param saveUserDto
     * @return
     */
    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @PostMapping("/saveUser")
    public ResponseEntity<ServerResponseDto> saveUser(@RequestBody SaveUserDto saveUserDto) {
        boolean isSaveSuccess = userService.saveUser(saveUserDto);
        return isSaveSuccess ? ResponseEntity.ok(new ServerResponseDto(ResponseCase.SUCCESS))
                : ResponseEntity.ok(new ServerResponseDto(ResponseCase.ERROR));
    }
}
