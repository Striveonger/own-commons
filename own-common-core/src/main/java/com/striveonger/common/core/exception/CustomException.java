package com.striveonger.common.core.exception;

import com.striveonger.common.core.constant.ResultStatus;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-12 13:12
 */
public class CustomException extends RuntimeException {

    private final ResultStatus status;

    private String message;

    public CustomException(ResultStatus status) {
        super(status.getMessage());
        this.status = status;
        this.message = status.getMessage();
    }

    public CustomException(String message) {
        super(message);
        this.status = ResultStatus.ACCIDENT;
        this.message = message;
    }

    public CustomException(ResultStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public ResultStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static CustomException create(ResultStatus status) {
        return new CustomException(status);
    }

    public static CustomException create(String message) {
        return new CustomException(message);
    }

    public CustomException message(String message) {
        this.message = message;
        return this;
    }

    public CustomException show() {
        this.status.show();
        return this;
    }
}