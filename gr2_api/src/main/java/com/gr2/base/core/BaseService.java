package com.gr2.base.core;

import com.gr2.base.security.CurrentUserDetailsContainer;
import com.gr2.custom.entity.UserEntity;
import com.gr2.custom.repository.UserRepository;
import com.gr2.custom.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author dainv
 */
@Service
public abstract class BaseService<E extends BaseEntity, R extends BaseRepository> {

    @Autowired
    protected R repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserDetailsContainer currentUserDetailsContainer;

    public CustomUserDetails getCurrentUser() {
        return this.currentUserDetailsContainer.getUserDetails();
    }

    /**
     * Create
     */
    public E save(E entity) {
        if (entity == null) {
            return null;
        }

        preSave(entity);

        return (E) repository.save(entity);
    }

    public void preSave(E entity) {
        /**
         * gen UUID code
         */

        if (getCurrentUser() != null) {
            if (entity.getId() == null) {
                entity.setCreatedByUserId(getCurrentUser().getUserId());
            }
            entity.setUpdatedByUserId(getCurrentUser().getUserId());
        }
        if (entity.getId() == null) {
            entity.setCreatedTime(new Date());
        }
        entity.setUpdatedTime(new Date());
    }



    /**
     * Service Read
     */

    /**
     * Service Read
     */
    public E findById(Long id) {
        Object entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }

        return (E) entity;
    }

    /**
     * Update
     */
    public E update(E entity) {
        if (entity == null || entity.getId() == null) {
            return null;
        } else {
            entity.setUpdatedTime(new Date());
            if (getCurrentUser() != null) {
                entity.setUpdatedByUserId(getCurrentUser().getUserId());
            }
            return (E) repository.save(entity);
        }
    }

    /**
     * Delete
     */
    public boolean delete(Long id) {
        Object entityObj = repository.findById(id);
        if (entityObj == null) {
            return false;
        } else {
            E entity = (E) entityObj;
            entity.setDeleted(true); // logic delete
            if (getCurrentUser() != null) {
                entity.setUpdatedByUserId(getCurrentUser().getUserId());
            }
            repository.save(entity);
            return true;
        }
    }

    /**
     * Get findAll
     *
     * @param page
     * @return
     */
    public Page<E> findAll(Pageable page, boolean isDeleted) {
        return repository.findByIsDeleted(page, isDeleted);
    }

    public Page<E> setCreatorAndUpdater(Page<E> page) {
        if (page.getContent()
                .isEmpty()) {
            return page;
        }
        Set<Long> listUserId = page.stream()
                .flatMap(e -> Stream.of(e.getUpdatedByUserId(), e.getCreatedByUserId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> mapUsernameById = userRepository
                .findAllByIdIn(listUserId)
                .stream()
                .collect(Collectors.toMap(UserEntity::getId, UserEntity::getName));
        return page.map(e -> {
                    e.setNameCreator(mapUsernameById.get(e.getCreatedByUserId()));
                    e.setNameUpdater(mapUsernameById.get(e.getUpdatedByUserId()));
                    return e;
                }
        );
    }

}
