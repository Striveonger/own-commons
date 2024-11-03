package com.striveonger.common.ext.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @since 2024-10-27 21:26
 */
@ApiPreset
public class V3 {
    private final Logger log = LoggerFactory.getLogger(V3.class);

    public String hello() {
        return "hello";
    }
}
