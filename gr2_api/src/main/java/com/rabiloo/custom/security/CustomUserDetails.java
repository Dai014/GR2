package com.rabiloo.custom.security;

import com.rabiloo.custom.entity.UserEntity;
import com.rabiloo.custom.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
public class CustomUserDetails extends User {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String role;
    private Long appId;
    private String accessToken;
    private String refreshToken;
    private String redirectUrl;
    private Long pcrMasterId;
    private String memberCode;

    CustomUserDetails(String password, Collection<? extends GrantedAuthority> authorities,
                      Long userId, String userName, String role) {
        super(userName, password, true, true, true, true, authorities);
        this.userId = userId;
        this.role = role;
    }

    public CustomUserDetails(String memberCode, String password, Long userId, String role, Long appId,
                             String accessToken, String refreshToken,
                             Collection<? extends GrantedAuthority> authorities, Long pcrMasterId) {
        super(memberCode, password, true, true, true, true, authorities);
        this.userId = userId;
        this.role = role;
        this.appId = appId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.pcrMasterId = pcrMasterId;
        this.memberCode = memberCode;
    }

    public static CustomUserDetails createForWebUser(UserEntity user) {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getValue()));

        return new CustomUserDetails(user.getPassword(), authorities, user.getId(), user.getUsername(), user.getRole().getValue());
    }

}
