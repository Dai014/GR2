package com.rabiloo.custom.dto.request;

import com.rabiloo.custom.enums.WebUserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUserDto {

    private Long id;
    private String username;
    private String password;
    private WebUserRole role;
    private String name;
    private String email;
    private String phone;
}
