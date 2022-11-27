package com.gr2.custom.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

public class PageableUtils {

    public static final String ASC_DIR = "ASC";

    public static Pageable from(String sortDir, String sortField, int page, int size) {
        if (ASC_DIR.equalsIgnoreCase(sortDir)) {
            return PageRequest.of(page - 1, size, Sort.Direction.ASC, sortField);
        }

        return PageRequest.of(page - 1, size, Sort.Direction.DESC, sortField);
    }

    public Pageable fromWithJpaSortUnsafe(String sortDir, String sortField, int page, int size) {
        Sort sort;
        if (ASC_DIR.equalsIgnoreCase(sortDir)) {
            sort = JpaSort.unsafe(Sort.Direction.ASC, "(" + sortField + ")");
        } else {
            sort = JpaSort.unsafe(Sort.Direction.DESC, "(" + sortField + ")");
        }
        return PageRequest.of(page - 1, size, sort);
    }

}
