package com.striveonger.common.storage.context;

import com.striveonger.common.storage.config.StorageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @since 2024-07-26 01:27
 */
public class MinioStorage implements Storage {
    private final Logger log = LoggerFactory.getLogger(MinioStorage.class);

    private final MinioHelper helper;

    private final String bucket;

    public MinioStorage(StorageConfig.MinioStoreConfig config) {
        this.helper = new MinioHelper(config);
        this.bucket = config.getBucket();
    }

    @Override
    public byte[] read(String path) {
        return helper.getObject(bucket, path);
    }

    @Override
    public void write(String path, byte[] bytes) {
        helper.saveObject(bucket, path, bytes);
    }

    @Override
    public void delete(String path) {
        helper.deleteObject(bucket, path);
    }
}
