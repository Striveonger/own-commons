package com.striveonger.common.leaf.core;

import com.striveonger.common.core.exception.CustomException;

/**
 * ID生成器
 * @author Mr.Lee
 * @since 2024-08-15 23:02
 */
public interface IDGen {
    // private final Logger log = LoggerFactory.getLogger(IDGen.class);

    static Builder builder() {
        return new Builder();
    }

    class Builder {
        /**
         * 生成器类型(通用属性)
         */
        private String type;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public IDGen build() {
            if ("snowflake".equals(type)) {
                // 雪花算法


            } else if ("segment".equals(type)) {
                // 号段模式
            }
            throw new CustomException("There is a problem with startup mode...");
        }
    }










}
