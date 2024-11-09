package com.striveonger.common.storage.context;

import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.storage.config.StorageConfig;

import java.util.Objects;

import static com.striveonger.common.storage.context.Storage.StorageType.FILE;
import static com.striveonger.common.storage.context.Storage.StorageType.MINIO;

/**
 * 存储器
 *
 * @author Mr.Lee
 * @since 2024-07-26 00:18
 */
public interface Storage {

    /**
     * 读取文件
     *
     * @param url 相对路径
     * @return 文件
     */
    byte[] read(String url);

    /**
     * 写入文件
     *
     * @param url   文件名
     * @param bytes 字节数组
     * @return 相对路径
     */
    void write(String url, byte[] bytes);

    /**
     * 删除文件
     *
     * @param url 相对路径
     */
    void delete(String url);


    class Factory {
        private static final Object lock = new Object();

        private static StorageConfig config;

        private static FileStorage fileStorage = null;

        private static MinioStorage minioStorage = null;

        /**
         * 初始化工厂对象
         *
         * @param config 配置类
         */
        public static void init(StorageConfig config) {
            synchronized (lock) {
                if (config != null && Factory.config == null) {
                    Factory.config = config;
                    if (Objects.nonNull(config.getMinio())) {
                        Factory.minioStorage = new MinioStorage(config.getMinio());
                    }
                    if (Objects.nonNull(config.getFile())) {
                        Factory.fileStorage = new FileStorage(config.getFile());
                    }
                }
            }
        }

        public static Storage of(StorageType type) {
            if (type == FILE && Objects.nonNull(fileStorage)) {
                return fileStorage;
            }
            if (type == MINIO && Objects.nonNull(minioStorage)) {
                return minioStorage;
            }
            throw CustomException.of(ResultStatus.NON_SUPPORT).message("不支持的存储类型");
        }
    }

    enum StorageType {
        FILE("file"),
        MINIO("minio");

        private final String name;

        StorageType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static StorageType of(String name) {
            for (StorageType value : values()) {
                if (value.name.equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }
    }
}
