package com.gr2.base.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class BaseDomain {

    @JsonIgnore
    protected Long id;

    protected UUID code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

}
