package com.striveonger.common;

import com.striveonger.common.ext.annotation.ApiPreset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @since 2024-10-27 20:47
 */
@ApiPreset
public class V2 {
    private final Logger log = LoggerFactory.getLogger(V2.class);

    public String hello() {
        return "hello";
    }

    public String helloX(String x) {
        return "hello " + x;
    }
}
