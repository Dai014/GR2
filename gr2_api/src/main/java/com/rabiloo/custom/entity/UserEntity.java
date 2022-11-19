package com.rabiloo.custom.entity;

import com.rabiloo.base.core.BaseEntity;
import com.rabiloo.custom.enums.WebUserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

@Entity
@Table(name = "user")
@Where(clause = "is_deleted=false")
@Getter
@Setter
@ToString
public class UserEntity extends BaseEntity {

    private String username;
    private String password;
    private String defaultPassword;

    @Convert(converter = WebUserRole.Converter.class)
    private WebUserRole role;
    private String name;

    private boolean isChangePassword;
    private String email;
    private String phone;
    private boolean isActive;

    @Transient
    private String createdByUserName;
    @Transient
    private String updatedByUserName;

    public UserEntity() {
    }

    public UserEntity(String username, String password, String name, String email, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public boolean isFirstLogin() {
        return !isChangePassword;
    }

    public UserEntity(UserEntity u) {
        this.id = u.id;
        this.username = u.username;
        this.password = u.password;

        this.role = u.role;
        this.name = u.name;
        this.isChangePassword = u.isChangePassword;
        this.email = u.email;
        this.phone = u.phone;
        this.isActive = u.isActive;
        this.createdTime = u.createdTime;
        this.updatedTime = u.updatedTime;
        this.createdByUserId = u.createdByUserId;
        this.updatedByUserId = u.updatedByUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
