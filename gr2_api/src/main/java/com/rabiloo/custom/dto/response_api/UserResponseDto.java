package com.rabiloo.custom.dto.response_api;

import com.rabiloo.custom.enums.WebUserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String username;
    private Long id;
    private WebUserRole role;
}
