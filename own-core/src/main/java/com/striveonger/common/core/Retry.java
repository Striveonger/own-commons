package com.striveonger.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * @author Mr.Lee
 * @since 2024-10-20 17:10
 */
public class Retry {
    private static final Logger log = LoggerFactory.getLogger(Retry.class);

    /**
     * 重试
     * @param supplier     执行函数
     * @param times        重试次数
     * @param defaultValue 默认值
     * @param <T>          回调返回值类型
     * @return 回调返回值
     */
    public static <T> T execute(Supplier<T> supplier, int times, T defaultValue) {
        boolean retry;
        T result = null;
        int count = 0;
        do {
            try {
                result = supplier.get();
                retry = false;
            } catch (Exception e) {
                retry = true;
                log.error(String.format("第%d执行失败", ++count), e);
            }
        } while (retry && count < times);
        if (retry) {
            return defaultValue;
        }
        return result;
    }

    /**
     * 重试
     *
     * @param supplier 执行函数
     * @param <T>      回调返回值类型
     * @return 回调返回值
     */
    public static <T> T execute(Supplier<T> supplier) {
        return execute(supplier, 3, null);
    }
}
