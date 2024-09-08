package com.striveonger.common.storage.context;

import com.striveonger.common.storage.config.StorageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-26 01:27
 */
public class MinioStorage implements Storage {
    private final Logger log = LoggerFactory.getLogger(MinioStorage.class);

    public MinioStorage(StorageConfig.MinioStoreConfig config) {

    }

    @Override
    public byte[] read(String url) {
        return null;
    }

    @Override
    public String write(String url, byte[] bytes) {

        return "";
    }

    @Override
    public void delete(String url) {

    }
}
