package com.striveonger.common.third.redis;

import com.striveonger.common.core.Jackson;
import com.striveonger.common.core.result.Result;
import com.striveonger.common.core.thread.ThreadKit;
import com.striveonger.common.third.redis.config.RedisConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RedisKitTest {

    private RedisKit redis;

    @Before
    public void setup() {
        RedisConfig config = new RedisConfig();
        config.setPassword("123456");
        config.setUsername("redis");
        RedisConfig.Single single = new RedisConfig.Single();
        single.setAddress("redis://10.13.144.104:6379");
        single.setDatabase(0);
        config.setSingle(single);
        this.redis = RedisKit.builder().config(config).build();
    }

    @Test
    public void test() {
        Result result = Result.success();
        redis.putValue("xxx", result);
    }

    @Test
    public void test1() {
        Result result = redis.getValue("xxx");
        System.out.println(Jackson.toJSONString(result));
    }

    @Test
    public void test2() {
        Result result = redis.getValueAndRemove("xxx");
        System.out.println(Jackson.toJSONString(result));
    }

    @Test
    public void test3() {
        String key = "key" + System.currentTimeMillis();
        RedisKit.Lock lock = redis.getLock();
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = ThreadKit.run(() -> {
                try {
                    lock.lock(key);
                    ThreadKit.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " 获得锁, 当前时间戳: " + System.currentTimeMillis());
                } finally {
                    lock.unlock(key);
                }
            }, String.format("t%02d", i), false);
            list.add(thread);
        }
        list.forEach(Thread::start);
        // 等待所有线程执行完毕
        list.forEach(ThreadKit::join);
    }
}