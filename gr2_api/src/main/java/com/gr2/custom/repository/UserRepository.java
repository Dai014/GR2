package com.gr2.custom.repository;

import com.gr2.base.core.BaseRepository;
import com.gr2.custom.entity.UserEntity;
import com.gr2.custom.enums.WebUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserRepository extends BaseRepository<UserEntity> {

    UserEntity findUserByUsernameAndIsDeletedFalse(String username);

    UserEntity findByUsernameAndRoleAndIsDeletedFalse(String userName, WebUserRole role);

    UserEntity findByIdAndIsDeletedFalse(Long userId);

    UserEntity findByUsername(String username);

    @Query(value = "select u.username from UserEntity u where u.username is not null")
    Set<String> getAllUsername();

    @Query(value = "select new UserEntity(u) " +
            "from UserEntity u " +
            "where u.isDeleted = false " +
            "and (?1 is null " +
            "or (u.name like concat('%', ?1, '%')) or (u.username like concat('%', ?1, '%'))" +
            "or u.email like concat('%', ?1, '%') or u.phone like concat('%', ?1, '%'))")
    Page<UserEntity> findUser(String keyword, Pageable pageable);

    List<UserEntity> findAllByIdIn(Collection<Long> ids);
}
