package com.striveonger.common.web.handler;

import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Mr.Lee
 * @since 2022-11-12 12:38
 */
@ControllerAdvice
public class WebExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public Result customExceptionHandler(CustomException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return Result.status(e.getStatus()).message(e.getMessage()).show(e.getStatus().getShow());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        return Result.accident().message(e.getMessage());
    }
}