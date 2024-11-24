package com.striveonger.common.leaf.config;

import java.util.Objects;

import static com.striveonger.common.leaf.core.snowflake.SnowflakeIDGen.DEFAULT_WORKER_ID;
import static com.striveonger.common.leaf.core.snowflake.SnowflakeIDGen.MAX_WORKER_ID;

/**
 * Leaf 配置类
 * @author Mr.Lee
 * @since 2024-09-06 11:24
 */
public class LeafConfig {

    private int workerId = DEFAULT_WORKER_ID;

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        if (Objects.nonNull(workerId) && workerId >= 0 && workerId < MAX_WORKER_ID) {
            this.workerId = workerId;
        }
    }
}
