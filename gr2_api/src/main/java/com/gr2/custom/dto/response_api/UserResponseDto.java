package com.gr2.custom.dto.response_api;

import com.gr2.custom.enums.WebUserRole;
import lombok.Getter;
import lombok.Setter;

/**
 * UserResponseDto
 * return user dto
 */
@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String username;
    private Long id;
    private WebUserRole role;
}
