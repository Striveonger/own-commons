package com.striveonger.common.store.context;

import cn.hutool.core.util.StrUtil;

/**
 * @author Mr.Lee
 * @description: 存储器
 * @date 2024-07-26 00:18
 */
public interface Storer {

    /**
     * 读取文件
     * @param url 相对路径
     * @return 文件
     */
    byte[] read(String url);

    /**
     * 写入文件
     * @param filename 文件名
     * @param bytes    字节数组
     * @return 相对路径
     */
    String write(String filename, byte[] bytes);


    class Builder {
        private String type;

        public static Builder builder() {
            return new Builder();
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Storer build() {
            if (StrUtil.equalsIgnoreCase(type, "minio")) {
                return new MinioObjectStorer();
            } else if (StrUtil.equalsIgnoreCase(type, "aliyun")) {
                return new AiliyunObjectStorer();
            } else {
                return new FileStorer();
            }
        }

    }
}
