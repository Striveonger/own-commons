package com.striveonger.common.storage.context;

import cn.hutool.core.io.FileUtil;
import com.striveonger.common.storage.config.StorageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * 文件存储器
 * @author Mr.Lee
 * @since 2024-07-26 01:25
 */
public class FileStorage implements Storage {
    private final Logger log = LoggerFactory.getLogger(FileStorage.class);

    private final String storageFolder;

    public FileStorage(StorageConfig.FileStoreConfig config) {
        this.storageFolder = config.getFolder();
    }

    @Override
    public byte[] read(String url) {
        return FileUtil.readBytes(storageFolder + url);
    }

    @Override
    public void write(String url, byte[] bytes) {
        String path = storageFolder + url;
        FileUtil.writeBytes(bytes, path);
    }

    @Override
    public void delete(String url) {
        boolean exists = FileUtil.exists(Path.of(storageFolder + url), false);
        if (exists) {
            FileUtil.del(storageFolder + url);
        }
    }
}
