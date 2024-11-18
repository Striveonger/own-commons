package com.striveonger.common.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.striveonger.common.core.thread.ThreadContext;
import com.striveonger.common.core.thread.ThreadKit;
import com.striveonger.common.core.thread.ThreadPool;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

public class ThreadKitTest {


    @Test
    public void testPool() {
        // 线程池 最多支持 2个线程, 队列最多支持 2个任务
        ThreadPool pool = ThreadKit.pool().corePoolSize(1).maximumPoolSize(8).maximumQueueSize(2).build();
        List<Future<?>> list = CollUtil.newArrayList();
        for (int i = 0; i < 10; i++) {
            final int idx = i;
            Future<?> future = pool.submit(() -> {
                System.out.printf("task time: %s, idx: %d, name: %s \n", DateUtil.now(), idx, Thread.currentThread().getName());
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

    @Test
    public void testContext() {
        // 1. 单线程上下文
        try (ThreadContext<Object> context = ThreadKit.context()) {
            context.set("123456");
            System.out.println(context.get());
            ThreadKit.sleep(1000);
            context.set("456789");
            System.out.println(context.get());
            System.out.println("------------------------------------------------------------------");
        } finally {
            System.out.println("finally");
        }
        System.out.println();

        // 2. 多线程上下文
        try (ThreadContext<Object> context = ThreadKit.context()) {
            context.set("123456");
            System.out.println(ThreadKit.name() + ": " + context.get());      // 123456
            ThreadKit.run(() -> {
                System.out.println(ThreadKit.name() + ": " + context.get());  // 123456
                ThreadKit.sleep(1000);
                context.set("456789");
                System.out.println(ThreadKit.name() + ": " + context.get());  // 456789, 只能向下传递, 不能向传递
                ThreadKit.run(() -> {
                    System.out.println(ThreadKit.name() + ": " + context.get());  // 456789
                }, "t-sub", true);
            }, "t", true);
            ThreadKit.sleep(2000);
            System.out.println(ThreadKit.name() + ": " + context.get());      // 123456
            System.out.println("------------------------------------------------------------------");
        }
    }

    @Test
    public void testBuffer() {
        String key = "A";
        ThreadKit.buffer().put(key, "123456");
        String s = ThreadKit.buffer().get(key);
        System.out.println(ThreadKit.name() + ", A: " + s);
        ThreadKit.run(() -> {
            String s1 = ThreadKit.buffer().get(key);
            System.out.println(ThreadKit.name() + ", A: " + s1);
            ThreadKit.buffer().put(key, "456789");
        }, "test", true);
        ThreadKit.sleep(2000);

        s = ThreadKit.buffer().get(key);
        System.out.println(ThreadKit.name() + ", A: " + s);
    }


    private volatile int exitCode = 1;
    @Test
    public void testQueue() {
        // 生产者1
        ThreadKit.run(() -> {
            for (int i = 1; i <= 3; i++) {
                ThreadKit.queue().offer("x: " + i);
                ThreadKit.sleep(500);
            }
        }, "producer1", true);

        // 生产者2
        ThreadKit.run(() -> {
            for (int i = 4; i <= 9; i++) {
                ThreadKit.queue().offer("x: " + i);
                ThreadKit.sleep(200);
            }
        }, "producer2", true);

        // 消费者1
        ThreadKit.run(() -> {
            while (exitCode == 1) {
                String x = ThreadKit.queue().take();
                if (Objects.nonNull(x)) {
                    System.out.println(DateUtil.current() + " --->> " + x);
                }
            }
        }, "consumer1", true);

        ThreadKit.sleep(2500);
        exitCode = 0;
    }

}