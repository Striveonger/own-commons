package com.striveonger.common.core.web.vo;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-12 14:58
 */
public class BasicQueryVo {

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