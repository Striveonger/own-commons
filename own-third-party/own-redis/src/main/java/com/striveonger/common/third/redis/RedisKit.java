package com.striveonger.common.third.redis;

import cn.hutool.core.util.SerializeUtil;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.third.redis.config.RedisConfig;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Lee
 * @since 2024-12-01 12:53
 */
public class RedisKit {
    private final Logger log = LoggerFactory.getLogger(RedisKit.class);

    /**
     * 客户端
     */
    private final RedissonClient client;

    /**
     * 分布式锁
     */
    private final Lock lock;

    private RedisKit(RedissonClient client) {
        this.client = client;
        this.lock = new Lock();
    }

    public <T extends Serializable> void putValue(String key, T val) {
        byte[] bytes = SerializeUtil.serialize(val);
        setBytes(key, bytes);
    }

    public <T extends Serializable> T getValue(String key) {
        byte[] bytes = getBytes(key);
        return bytes == null ? null : SerializeUtil.deserialize(bytes);
    }

    public void removeValue(String key) {
        RBucket<Object> bucket = client.getBucket(key);
        bucket.delete();
    }

    public <T extends Serializable> T getValueAndRemove(String key) {
        RBucket<byte[]> bucket = client.getBucket(key);
        byte[] bytes = bucket.getAndDelete();
        return bytes == null ? null : SerializeUtil.deserialize(bytes);
    }

    public void setBytes(String key, byte[] bytes) {
        RBucket<byte[]> bucket = client.getBucket(key);
        bucket.set(bytes);
    }

    public byte[] getBytes(String key) {
        RBucket<byte[]> bucket = client.getBucket(key);
        return bucket.get();
    }














    /**
     * 获取分布式锁
     * @return 分布式锁
     */
    public Lock lock() {
        return lock;
    }

    /**
     * RedisKit 构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 分布式锁(Redisson实现)
     */
    public class Lock {


        private Lock() { }

        /**
         * <p>使用 Redis 的 SETNX 命令尝试获取锁. SETNX 命令会在 Redis 中设置一个键值对, 仅当键不存在时才会成功, 并返回 1, 表示成功获取到锁.<p/>
         * <p>如果获取锁成功, 则设置锁的过期时间, 以防止锁无法释放. 这可以通过 Redis 的 PEXPIRE 命令来实现.<p/>
         * <p>如果获取锁失败, 则使用 Redis 的 BLPOP 命令在一个特定的队列上等待锁的释放. BLPOP 是一个阻塞式命令, 它会一直阻塞直到队列中有元素可弹出.<p/>
         * <p>当锁的持有者释放锁时, Redisson 会使用 Redis 的 DEL 命令来删除锁.<p/>
         * <p>Redisson 还提供了以下额外的特性来增强分布式锁的功能和可靠性<p/>
         * <p>可重入锁: Redisson 支持可重入锁, 允许同一个线程多次获取同一个锁, 避免死锁问题.<p/>
         * <p>公平锁: Redisson 提供了公平锁的实现, 保证锁的获取按照申请的顺序进行, 避免线程饥饿问题.<p/>
         * <p>读写锁: Redisson 还支持读写锁, 允许多个线程同时读取共享资源, 但只允许一个线程进行写操作.<p/>
         * <p>锁的续期: Redisson 允许在持有锁的期间自动续期锁的过期时间, 确保业务逻辑执行时间超过锁的过期时间时不会释放锁.<p/>
         * <p>监听锁的释放事件: Redisson 支持监听锁的释放事件, 以便在锁被释放时执行一些特定的逻辑.<p/>
         * <p>使用 Redisson 实现分布式锁非常简单, 只需要引入 Redisson 的依赖, 并使用 Redisson 的 RLock 接口来进行锁的获取和释放.<p/>
         */
        private static final String LOCK_PREFIX = "own:lock:";
        private static final int DEFAULT_USE_TIME = 30 * 1000; // default 30s;

        /**
         * 尝试获得锁, 如果没有获得锁,会阻塞等待获得锁
         * @param key 锁标识
         */
        public void lock(String key) {
            lock(key, DEFAULT_USE_TIME);
        }

        /**
         * 尝试获得锁, 如果没有获得锁,会阻塞等待获得锁
         * @param key     锁标识
         * @param usetime 最长使用时间(毫秒)
         */
        public void lock(String key, long usetime) {
            key = LOCK_PREFIX + key;
            RLock lock = client.getLock(key);
            lock.lock(usetime, TimeUnit.MILLISECONDS);
        }

        /**
         * 尝试获得锁
         * @param key      锁标识
         * @param waitTime 最长等待时间(毫秒)
         * @return         是否获得锁
         */
        public boolean tryLock(String key, long waitTime) {
            return tryLock(key, DEFAULT_USE_TIME, waitTime);
        }

        /**
         * 尝试获得锁
         * @param key      锁标识
         * @param usetime  最大使用时长(毫秒)
         * @param waitTime 最大等待时长(毫秒)
         * @return         是否获得锁
         */
        public boolean tryLock(String key, long usetime, long waitTime) {
            key = LOCK_PREFIX + key;
            RLock lock = client.getLock(key);
            try {
                return lock.tryLock(waitTime, usetime, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.error("An attempt to acquire a lock failed... ", e);
            }
            return false;
        }


        /**
         * 释放锁
         * @param key 锁标识
         */
        public void unlock(String key) {
            synchronized (key.intern()) {
                key = LOCK_PREFIX + key;
                RLock lock = client.getLock(key);
                if (Objects.nonNull(lock) && lock.isLocked()) {
                    lock.unlock();
                }
            }
        }
    }

    public static class Builder {

        private RedisConfig rc;

        private Builder() {}

        public Builder config(RedisConfig rc) {
            this.rc = rc;
            return this;
        }

        public RedisKit build() {
            Config config = new Config();
            // 单节点模式
            if (Objects.nonNull(rc.getSingle())) {
                config.useSingleServer()
                        .setAddress(rc.getSingle().getAddress())
                        .setDatabase(rc.getSingle().getDatabase())
                        .setUsername(rc.getUsername())
                        .setPassword(rc.getPassword());
                return new RedisKit(Redisson.create(config));
            }
            // TODO: 后面再扩展其他模式

            throw CustomException.of(ResultStatus.NON_SUPPORT).message("不支持的Redis模式");
        }
    }
}
