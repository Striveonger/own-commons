package com.striveonger.common.mybatis.utils;

import com.mybatisflex.core.paginate.Page;
import com.striveonger.common.core.result.Result;

import java.util.function.Function;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-04-09 11:37
 */
public class PageUtil {

    public static <T, R> Result.Page<R> convertResultPage(Page<T> page, Function<T, R> converter) {
        Result.Page<R> result = new Result.Page<>();
        result.setForm(page.getPageNumber());
        result.setSize(page.getPageSize());
        result.setList(page.getRecords().stream().map(converter).toList());
        result.setTotal(page.getTotalRow());
        return result;
    }

    public static <T> Result.Page<T> convertResultPage(Page<T> page) {
        Result.Page<T> result = new Result.Page<>();
        result.setForm(page.getPageNumber());
        result.setSize(page.getPageSize());
        result.setList(page.getRecords());
        result.setTotal(page.getTotalRow());
        return result;
    }
}