package com.striveonger.common.core;

import cn.hutool.core.collection.CollUtil;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Future;

public class ThreadKitTest {


    @Test
    public void test() {
        // 线程池 最多支持 2个线程, 队列最多支持 2个任务
        ThreadKit.Pool pool = ThreadKit.pool().corePoolSize(1).maximumPoolSize(8).maximumQueueSize(2).build();
        List<Future<?>> list = CollUtil.newArrayList();
        for (int i = 0; i < 10; i++) {
            final int idx = i;
            Future<?> future = pool.submit(() -> {
                System.out.printf("task idx: %d, name: %s \n", idx, Thread.currentThread().getName());
                ThreadKit.sleep(1000);
            });
            list.add(future);
            // System.out.println("submit task idx:" + idx);
            // ThreadKit.sleep(180);
        }
        System.out.println("submit all task");
        pool.await();
        // pool.shutdown();
        // boolean await = pool.await(6000, TimeUnit.MILLISECONDS);
        // System.out.println(await);
    }
}