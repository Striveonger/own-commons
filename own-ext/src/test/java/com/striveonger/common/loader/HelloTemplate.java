package com.striveonger.common.loader;

import com.striveonger.common.core.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @since 2024-11-05 21:51
 */
public class HelloTemplate implements Template {
    private final Logger log = LoggerFactory.getLogger(HelloTemplate.class);

    @Override
    public Result list(Map<String, String> map) {
        return null;
    }
}
