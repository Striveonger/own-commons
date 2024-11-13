package com.striveonger.common.core.vo;

/**
 * 基础检索
 * @author Mr.Lee
 * @since 2024-09-13 22:43
 */
public class BasicVo {

    private Integer from;

    private Integer size;

    private String search;

    public Integer getSize() {
        return this.size == null || size <= 0 ? 15 : size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getFrom() {
        return from == null || from <= 0 ? 1 : from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
