/*
 *  ExceptionHandlerControllerAdvice.java
 *
 *  Copyright (c) 2020 JSC
 */
package com.rabiloo.custom.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public void handleAccessDeniedException(HttpServletRequest request) {
        log.error(request.getRequestURI());
    }

}
