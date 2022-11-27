package com.gr2.custom.service.web_user;

import com.gr2.base.core.BaseService;
import com.gr2.custom.dto.request.SaveUserDto;
import com.gr2.custom.dto.response_api.ResponseCase;
import com.gr2.custom.dto.response_api.ServerResponseDto;
import com.gr2.custom.dto.response_api.UserResponseDto;
import com.gr2.custom.entity.UserEntity;
import com.gr2.custom.enums.WebUserRole;
import com.gr2.custom.repository.UserRepository;
import com.gr2.custom.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <h1>userService handles the logic related to the user </h1>
 */
@Service
public class UserService extends BaseService<UserEntity, UserRepository> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Page<UserEntity> findUser(String keyword, Pageable pageable) {
        return repository.findUser(keyword, pageable);
    }

    /**
     * @param oldPassword old password
     * @param newPassword new password
     * @param userId      Id of user need to change password
     * @author Dainv
     */
    public ServerResponseDto changePassword(String oldPassword, String newPassword, Long userId) {
        if (newPassword.equals(oldPassword) || !newPassword.matches(Constants.PASSWORD_PATTERN)) {
            LOGGER.info("New password is same with old password, or format is incorrect");
            return new ServerResponseDto(ResponseCase.INVALID_NEW_PASSWORD);
        }
        UserEntity userEntity = repository.findByIdAndIsDeletedFalse(userId);
        if (userEntity == null) {
            LOGGER.info("Cannot find userEntity with userId : " + userId);
            return new ServerResponseDto(ResponseCase.NOT_FOUND);
        }

        /* if old password is null then not check matches */
        if (!Objects.isNull(oldPassword) && !passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            LOGGER.info("OldPassword is incorrect");
            return new ServerResponseDto(ResponseCase.OLD_PASSWORD_IS_INCORRECT);
        }
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        this.save(userEntity);
        return new ServerResponseDto(ResponseCase.SUCCESS);
    }

    /**
     * @param newPassword new password
     * @param userId      Id of user need to change password
     * @author Dainv
     */
    public ServerResponseDto changePasswordFirst(String newPassword, Long userId) {
        UserEntity userEntity = repository.findByIdAndIsDeletedFalse(userId);
        if (userEntity == null) {
            LOGGER.info("Cannot find userEntity with userId : " + userId);
            return new ServerResponseDto(ResponseCase.NOT_FOUND);
        }

        if (!newPassword.matches(Constants.PASSWORD_PATTERN) || passwordEncoder.matches(newPassword, userEntity.getPassword())) {
            LOGGER.info("newPassword is same with oldPassword, or wrong format");
            return new ServerResponseDto(ResponseCase.INVALID_NEW_PASSWORD);
        }

        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userEntity.setChangePassword(true);
        save(userEntity);
        return new ServerResponseDto(ResponseCase.SUCCESS);
    }

    /**
     * find user has id ?1 and not deleted
     * @param id
     * @return
     */
    public UserEntity findByIdAndIsDeletedFalse(Long id) {
        return repository.findByIdAndIsDeletedFalse(id);
    }

    /**
     * find user has id ?1 and has role ?2
     * @param userName
     * @param role
     * @return
     */
    public UserEntity findByUsernameAndRole(String userName, WebUserRole role) {
        return repository.findByUsernameAndRoleAndIsDeletedFalse(userName, role);
    }

    /**
     * save user
     * @param saveUserDto
     * @return
     */
    public boolean saveUser(SaveUserDto saveUserDto) {
        Optional<UserEntity> userToSaveOpt = fromSaveDto(saveUserDto);
        if (userToSaveOpt.isEmpty()) {
            return false;
        }
        try {
            save(userToSaveOpt.get());
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("exception when save user: {}", e.getMessage());
            return false;
        }
    }

    private Optional<UserEntity> fromSaveDto(SaveUserDto saveUserDto) {
        String username = saveUserDto.getUsername();
        if (StringUtils.isEmpty(username)) {
            LOGGER.info("username must not be null");
            return Optional.empty();
        }

        UserEntity userToSave;

        boolean isUpdate = saveUserDto.getId() != null;
        if (isUpdate) {
            userToSave = repository.findByIdAndIsDeletedFalse(saveUserDto.getId());
            if (userToSave == null) {
                return Optional.empty();
            }
        } else {
            userToSave = new UserEntity();
        }

        if (StringUtils.isEmpty(saveUserDto.getPassword()) || saveUserDto.getPassword() == null) {
            userToSave.setPassword(passwordEncoder.encode(""));
        } else {
            userToSave.setPassword(passwordEncoder.encode(saveUserDto.getPassword()));
        }

        userToSave.setName(saveUserDto.getName());
        userToSave.setUsername(username);
        userToSave.setEmail(saveUserDto.getEmail());
        userToSave.setPhone(saveUserDto.getPhone());
        userToSave.setRole(saveUserDto.getRole());
        userToSave.setActive(true);

        return Optional.of(userToSave);
    }

    /**
     * get detail user has id ?1
     * @param userId
     * @return
     */
    public UserResponseDto getDetailUser(Long userId) {
        UserEntity userEntity = repository.findByIdAndIsDeletedFalse(userId);
        if (userEntity == null) {
            return null;
        }
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(userEntity.getId());
        userDto.setName(userEntity.getName());
        userDto.setUsername(userEntity.getUsername());
        userDto.setRole(userEntity.getRole());
        return userDto;
    }

    /**
     * delete user
     * @param userId
     * @return
     */
    public boolean delete(Long userId) {
        UserEntity user = repository.findByIdAndIsDeletedFalse(userId);
        if (user == null) {
            return false;
        }
        user.setDeleted(true);
        user.setUpdatedTime(new Date());
        repository.save(user);
        return true;
    }

    /**
     * reset password user
     * @param userId
     * @param newPassword
     * @return
     */
    public boolean resetPassword(Long userId, String newPassword) {
        UserEntity user = repository.findByIdAndIsDeletedFalse(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedTime(new Date());
        repository.save(user);
        return true;
    }

    /**
     * check username Exist
     * @param username
     * @return
     */
    public boolean isUsernameExist( String username) {
        UserEntity userEntity = repository.findByUsername(username);
        return userEntity != null;
    }

    public UserEntity validateSystemUser(String username, String password) {
        UserEntity userEntity = repository.findUserByUsernameAndIsDeletedFalse(username);
        if (userEntity == null || !userEntity.getRole().equals(WebUserRole.SYSTEM_ADMIN)) {
            LOGGER.info("Username in correct, cannot login!");
            return new UserEntity();
        }
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            LOGGER.info("Password is in correct, cannot login!");
            return new UserEntity();
        }
        return userEntity;
    }

    /**
     * delete multiple user
     * @param userIds
     * @return
     */
    public boolean deleteMultiple(List<Long> userIds) {
        List<UserEntity> userEntities =
                repository.findAllByIdIn(userIds);
        if (userEntities.isEmpty()) {
            return false;
        }
        userEntities.forEach(userEntity -> {
            userEntity.setDeleted(true);
            preSave(userEntity);
        });
        repository.saveAll(userEntities);
        return true;
    }

    /**
     * change Status User
     * @param userId
     * @return
     */
    public boolean changeStatusUser(Long userId) {
        UserEntity userEntity = repository
                .findByIdAndIsDeletedFalse(userId);
        if (userEntity == null) {
            return false;
        }

        userEntity.setActive(!userEntity.isActive());
        save(userEntity);
        return true;
    }

}
