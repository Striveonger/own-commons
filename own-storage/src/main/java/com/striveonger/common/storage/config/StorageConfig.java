package com.striveonger.common.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 存储配置
 * @author Mr.Lee
 * @since 2024-09-06 18:22
 */
@Configuration
@ConfigurationProperties(prefix = "own.store")
public class StorageConfig {

    private FileStoreConfig file;

    private MinioStoreConfig minio;

    public FileStoreConfig getFile() {
        return file;
    }

    public void setFile(FileStoreConfig file) {
        this.file = file;
    }

    public MinioStoreConfig getMinio() {
        return minio;
    }

    public void setMinio(MinioStoreConfig minio) {
        this.minio = minio;
    }

    public static class FileStoreConfig {
        /**
         * filestorage 所在目录
         */
        private String folder;

        public String getFolder() {
            return folder;
        }

        public void setFolder(String folder) {
            this.folder = folder;
        }
    }

    public static class MinioStoreConfig {
        private String endpoint;
        private String accessKey;
        private String secretKey;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }
}
