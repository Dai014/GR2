package com.rabiloo.custom.dto.response_api;

import com.rabiloo.custom.entity.UserEntity;

import java.util.Collection;

public class ImportUserDto {

    private Collection<UserEntity> listUser;
    private Collection<String> listUsernameError;

    public ImportUserDto(Collection<UserEntity> listUser,
                         Collection<String> listUsernameError) {
        this.listUser = listUser;
        this.listUsernameError = listUsernameError;
    }

    public Collection<UserEntity> getListUser() {
        return listUser;
    }

    public void setListUser(Collection<UserEntity> listUser) {
        this.listUser = listUser;
    }

    public Collection<String> getListUsernameError() {
        return listUsernameError;
    }

    public void setListUsernameError(Collection<String> listUsernameError) {
        this.listUsernameError = listUsernameError;
    }
}
