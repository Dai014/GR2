package com.gr2.custom.dto.request;

import com.gr2.custom.enums.WebUserRole;
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
