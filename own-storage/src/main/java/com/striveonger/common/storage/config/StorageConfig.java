package com.striveonger.common.storage.config;

/**
 * 存储配置
 * @author Mr.Lee
 * @since 2024-09-06 18:22
 */
public class StorageConfig {

    private Boolean enabled;

    private FileStoreConfig file;

    private MinioStoreConfig minio;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

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
        /**
         * minio 服务地址
         */
        private String endpoint;

        /**
         * minio 服务端口
         */
        private int port;

        /**
         * minio 服务是否使用https
         */
        private boolean secure;

        /**
         * minio 服务的访问key
         */
        private String accessKey;

        /**
         * minio 服务的访问密钥
         */
        private String secretKey;

        private String bucket;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public boolean getSecure() {
            return secure;
        }

        public void setSecure(boolean secure) {
            this.secure = secure;
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

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }
    }
}
