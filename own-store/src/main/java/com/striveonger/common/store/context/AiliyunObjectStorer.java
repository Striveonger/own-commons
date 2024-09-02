package com.striveonger.common.store.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-26 01:27
 */
public class AiliyunObjectStorer implements ObjectStorer {
    private final Logger log = LoggerFactory.getLogger(AiliyunObjectStorer.class);

    @Override
    public byte[] read(String url) {
        return null;
    }

    @Override
    public String write(String filename, byte[] bytes) {
        return "";
    }
}
