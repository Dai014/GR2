package com.rabiloo.base.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author kienta
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    protected Long id;

//    @JsonIgnore
    protected Date createdTime;

//    @JsonIgnore
    protected Date updatedTime;

    @JsonIgnore
    protected Long createdByUserId;

    @JsonIgnore
    protected Long updatedByUserId;

    @JsonIgnore
    protected boolean isDeleted;

    @Transient
    protected String nameCreator;

    @Transient
    protected String nameUpdater;

}
