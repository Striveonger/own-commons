package com.striveonger.common.core.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.striveonger.common.core.constant.ResultStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Mr.Lee
 * @description: 公用响应结果
 * @date 2022-11-08 21:36
 */
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态
     */
    private int state;

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private final LocalDateTime now = LocalDateTime.now();

    /**
     * 响应信息
     */
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    private Result() { }

    private Result(ResultStatus status) {
        this.state = status.getState();
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public Integer getState() {
        return state;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getNow() {
        return now;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return this.data;
    }

    public static Result success() {
        return new Result(ResultStatus.SUCCESS);
    }

    public static <T> Result success(T data) {
        return new Result(ResultStatus.SUCCESS).data(data);
    }

    public static <T> Result success(T data, String message) {
        return new Result(ResultStatus.SUCCESS).message(message).data(data);
    }

    public static <T> Result success(Page<T> page) {
        return new Result(ResultStatus.SUCCESS).page(page);
    }

    public static <T> Result success(Page<T> page, String message) {
        return new Result(ResultStatus.SUCCESS).message(message).page(page);
    }

    public static Result fail() {
        return new Result(ResultStatus.FAIL);
    }

    public static Result accident() {
        return new Result(ResultStatus.ACCIDENT);
    }

    public static Result status(ResultStatus status) {
        return new Result(status);
    }

    public static Result status(boolean status) {
        return new Result(status ? ResultStatus.SUCCESS : ResultStatus.FAIL);
    }

    public Result message(String message) {
        this.message = message;
        return this;
    }

    public <T> Result data(T data) {
        this.data = data;
        return this;
    }

    public <T> Result page(Page<T> page) {
        this.data = page;
        return this;
    }

    public static class Page<T> {
        private long form;
        private long size;
        private long total;
        private List<T> list;

        public long getForm() {
            return form;
        }

        public void setForm(long form) {
            this.form = form;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }
    }
}