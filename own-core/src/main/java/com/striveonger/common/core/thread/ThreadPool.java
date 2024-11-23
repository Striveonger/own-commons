package com.striveonger.common.core.thread;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.RejectPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

import static com.striveonger.common.core.thread.ThreadKit.sleep;

/**
 * @author Mr.Lee
 * @since 2024-11-17 11:36
 */
public class ThreadPool {
    private final Logger log = LoggerFactory.getLogger(ThreadPool.class);

    private final ThreadPoolExecutor executor;

    private ThreadPool(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    /**
     * 关闭线程池, 不再接受新的任务
     */
    public void shutdown() {
        this.executor.shutdown();
    }

    /**
     * 等待线程池中所有任务执行完毕 <br/>
     * 确保业务代码, 在调用此方法后, 不再有新的任务提交
     */
    public void await() {
        // 1. 等待任务队列被消费完
        while (CollUtil.isNotEmpty(this.executor.getQueue())) {
            sleep(10, TimeUnit.MILLISECONDS);
        }
        // 2. 等待所有任务执行完毕
        shutdown();
    }

    /**
     * 尝试在指定时间内, 等待线程池中所有任务执行完毕 {@link ThreadPoolExecutor#awaitTermination(long, TimeUnit)}
     * 所以, 确保在调用此方法前, 已经调用了{@link ThreadPoolExecutor#shutdown()}方法, 否则线程池不会执行完毕
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return 是否等待成功
     */
    public boolean await(long timeout, TimeUnit unit) {
        boolean awaited = false;
        try {
            // 等待线程池任务执行完毕
            awaited = executor.awaitTermination(timeout, unit);
            if (!awaited) {
                log.warn("等待超时，线程池未执行完毕！");
            }
        } catch (InterruptedException e) {
            log.error("{}, Interrupted...By ThreadKit", Thread.currentThread().getName(), e);
        }
        return awaited;
    }

    public void execute(Runnable runnable) {
        this.executor.execute(runnable);
    }

    public Future<?> submit(Runnable runnable) {
        return this.executor.submit(runnable);
    }

    public <T> Future<T> submit(Callable<T> callable) {
        return this.executor.submit(callable);
    }

    public <T> Future<T> submit(Runnable runnable, T result) {
        return this.executor.submit(runnable, result);
    }

    public static class Builder {
        /**
         * 线程池工作线程数
         */
        private Integer corePoolSize = 8;
        /**
         * 线程池最大容量
         */
        private Integer maximumPoolSize = 64;
        /**
         * 线程池最大队列容量
         */
        private Integer maximumQueueSize = 256;
        /**
         * 线程池空闲线程存活时间
         */
        private Long keepAliveTime = 30L;
        private TimeUnit unit = TimeUnit.SECONDS;
        /**
         * 线程池工作队列(默认使用LinkedBlockingQueue)
         */
        private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(maximumQueueSize);
        /**
         * 线程池工作线程工厂
         */
        private ThreadFactory threadFactory = Executors.defaultThreadFactory();
        /**
         * 线程池拒绝策略(重新插入到队列中, 会阻塞调用线程)
         */
        private RejectedExecutionHandler handler = RejectPolicy.BLOCK.getValue();

        private Builder() {
        }

        public static Builder of() {
            return new Builder();
        }

        public Builder corePoolSize(Integer corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public Builder maximumPoolSize(Integer maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
            return this;
        }

        /**
         * 设置队列长度, 用于存储未执行的任务
         * 使用此方法时, 会覆盖 `workQueue` 属性
         *
         * @param maximumQueueSize 队列长度
         * @return
         */
        public Builder maximumQueueSize(Integer maximumQueueSize) {
            this.maximumQueueSize = maximumQueueSize;
            if (maximumQueueSize <= 0) {
                this.workQueue = new SynchronousQueue<>();
            } else {
                this.workQueue = new LinkedBlockingQueue<>(maximumQueueSize);
            }
            return this;
        }

        /**
         * 设置队列，用于存在未执行的线程 可选队列有：
         * 1. SynchronousQueue    它将任务直接提交给线程而不保持它们。当运行线程小于maxPoolSize时会创建新线程，否则触发异常策略
         * 2. LinkedBlockingQueue 默认无界队列，当运行线程大于corePoolSize时始终放入此队列，此时maxPoolSize无效。
         * 当构造LinkedBlockingQueue对象时传入参数，变为有界队列，队列满时，运行线程小于maxPoolSize时会创建新线程，否则触发异常策略
         * 3. ArrayBlockingQueue  有界队列，相对无界队列有利于控制队列大小，队列满时，运行线程小于maxPoolSize时会创建新线程，否则触发异常策略
         *
         * @param workQueue 队列
         * @return this
         */
        public Builder workQueue(BlockingQueue<Runnable> workQueue) {
            this.workQueue = workQueue;
            return this;
        }

        public Builder keepAliveTime(Long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
            return this;
        }

        public Builder unit(TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        public Builder threadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        public Builder handler(RejectedExecutionHandler handler) {
            this.handler = handler;
            return this;
        }

        public ThreadPool build() {
            ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
            return new ThreadPool(executor);
        }
    }
}
