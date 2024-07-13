package com.striveonger.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-30 13:50
 */
public class SleepHelper {
    private final static Logger log = LoggerFactory.getLogger(SleepHelper.class);

    /**
     * 秒级阻塞
     * @param timeout
     */
    public static void sleepSeconds(int timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            log.error(Thread.currentThread().getName() + ", Interrupted...By SleepHelper", e);
        }
    }

    /**
     * 毫秒级阻塞
     * @param timeout
     */
    public static void sleepMilliSeconds(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            log.error(Thread.currentThread().getName() + ", Interrupted...By SleepHelper", e);
        }
    }

}