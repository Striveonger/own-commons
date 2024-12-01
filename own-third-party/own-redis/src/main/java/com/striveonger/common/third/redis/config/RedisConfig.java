package com.striveonger.common.third.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @since 2024-12-01 12:53
 */
public class RedisConfig {
    private final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    private String username;
    private String password;

    /**
     * TODO: 目前只支持单节点模式, 其他模式待开发
     * 模式选择
     * 后面需要什么配置, 再回来加呗, 参考:
     * 通用配置: {@link org.redisson.config.BaseConfig}
     * 单点模式: {@link org.redisson.config.SingleServerConfig}
     * 主从模式: {@link org.redisson.config.MasterSlaveServersConfig}
     * 哨兵模式: {@link org.redisson.config.SentinelServersConfig}
     * 集群模式: {@link org.redisson.config.ClusterServersConfig}
     */
    private Single single;
    private Maslaver maslaver;
    private Sentinel sentinel;
    private Custer cluster;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Single getSingle() {
        return single;
    }

    public void setSingle(Single single) {
        this.single = single;
    }

    public Maslaver getMaslaver() {
        return maslaver;
    }

    public void setMaslaver(Maslaver maslaver) {
        this.maslaver = maslaver;
    }

    public Sentinel getSentinel() {
        return sentinel;
    }

    public void setSentinel(Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public Custer getCluster() {
        return cluster;
    }

    public void setCluster(Custer cluster) {
        this.cluster = cluster;
    }

    /**
     * 单节点模式
     */
    public static class Single {
        private String address;
        public int database = 0; // 默认0号数据库呗

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDatabase() {
            return database;
        }

        public void setDatabase(int database) {
            this.database = database;
        }
    }

    /**
     * 主从模式
     */
    public static class Maslaver {
        // TODO: 主从模式
    }

    /**
     * 哨兵模式
     */
    public static class Sentinel {
        // TODO: 哨兵模式
    }

    /**
     * 集群模式
     */
    public static class Custer {
        // TODO 集群模式

    }

}
