package com.striveonger.common.leaf.core.segment;

import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.utils.Timepiece;
import com.striveonger.common.leaf.core.ID;
import com.striveonger.common.leaf.core.IDGen;
import com.striveonger.common.leaf.core.Status;
import com.striveonger.common.leaf.core.segment.model.Segment;
import com.striveonger.common.leaf.core.segment.model.SegmentBuffer;
import com.striveonger.common.leaf.entity.AllocEntity;
import com.striveonger.common.leaf.service.AllocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 号段分配, 生成ID
 *
 * @author Mr.Lee
 * @since 2024-08-17 00:28
 */
public class SegmentIDGen implements IDGen {
    private final Logger log = LoggerFactory.getLogger(SegmentIDGen.class);

    /**
     * IDCache未初始化成功时的异常码
     */
    private static final long EXCEPTION_ID_IDCACHE_INIT_FALSE = -1;
    /**
     * key不存在时的异常码
     */
    private static final long EXCEPTION_ID_KEY_NOT_EXISTS = -2;
    /**
     * SegmentBuffer中的两个Segment均未从DB中装载时的异常码
     */
    private static final long EXCEPTION_ID_TWO_SEGMENTS_ARE_NULL = -3;
    /**
     * 最大步长不超过100,0000
     */
    private static final int MAX_STEP = 1000000;
    /**
     * 一个Segment维持时间为15分钟
     */
    private static final long SEGMENT_DURATION = 15 * 60 * 1000L;

    private final ExecutorService threadPool = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new UpdateThreadFactory());

    private final Map<String, SegmentBuffer> cache = new ConcurrentHashMap<>();

    private static class UpdateThreadFactory implements ThreadFactory {

        private static int threadInitNumber = 0;

        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "Thread-Segment-Update-" + nextThreadNum());
        }
    }

    private final AllocService service;

    public SegmentIDGen(AllocService service) {
        this.service = service;
        // 初始化缓存
        refreshCacheFromDB();
        // 定时刷新缓存 schedule
        everyMinuteRefreshCacheFromDB();
    }

    @Override
    public ID next() {
        throw new CustomException(ResultStatus.NON_SUPPORT, "不支持该方法");
    }

    @Override
    public ID next(String tag) {
        if (cache.containsKey(tag)) {
            SegmentBuffer buffer = cache.get(tag);
            if (!buffer.isInitOk()) {
                synchronized (buffer) {
                    if (!buffer.isInitOk()) {
                        try {
                            updateSegmentFromDB(tag, buffer.getCurrent());
                            log.info("Init buffer. Update tag {} {} from db", tag, buffer.getCurrent());
                            buffer.setInitOk(true);
                        } catch (Exception e) {
                            log.warn("Init buffer {} exception", buffer.getCurrent(), e);
                        }
                    }
                }
            }
            return getIdFromSegmentBuffer(cache.get(tag));
        }
        return null;
    }

    private void refreshCacheFromDB() {
        log.info("update cache from db");
        // 计时开始～
        Timepiece timepiece = Timepiece.of("RefreshCacheFromDB");
        try {
            List<String> dbTags = service.listTags();
            if (dbTags == null || dbTags.isEmpty()) {
                return;
            }
            List<String> cacheTags = new ArrayList<>(cache.keySet());
            Set<String> insertTagsSet = new HashSet<>(dbTags);
            Set<String> removeTagsSet = new HashSet<>(cacheTags);
            // db中新加的tags灌进cache
            for (String tag : cacheTags) {
                insertTagsSet.remove(tag);
            }
            for (String tag : insertTagsSet) {
                SegmentBuffer buffer = new SegmentBuffer();
                buffer.setKey(tag);
                Segment segment = buffer.getCurrent();
                segment.setValue(new AtomicLong(0));
                segment.setMax(0);
                segment.setStep(0);
                cache.put(tag, buffer);
                log.info("Add tag {} from db to IdCache, SegmentBuffer {}", tag, buffer);
            }
            // cache中已失效的tags从cache删除
            for (String tag : dbTags) {
                removeTagsSet.remove(tag);
            }
            for (String tag : removeTagsSet) {
                cache.remove(tag);
                log.info("Remove tag {} from IdCache", tag);
            }
        } catch (Exception e) {
            log.warn("update cache from db exception", e);
        } finally {
            timepiece.show();
        }
    }

    private void everyMinuteRefreshCacheFromDB() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("check-id-cache-thread");
            thread.setDaemon(true);
            return thread;
        });
        service.scheduleWithFixedDelay(this::refreshCacheFromDB, 60, 60, TimeUnit.SECONDS);
    }

    public void updateSegmentFromDB(String tag, Segment segment) {
        // StopWatch sw = new StopWatch();
        SegmentBuffer buffer = segment.getBuffer();
        AllocEntity entity;
        if (!buffer.isInitOk()) {
            entity = service.getAlloc(tag);
            buffer.setStep(entity.getStep());
            buffer.setMinStep(entity.getStep());// leafAlloc中的step为DB中的step
        } else if (buffer.getUpdateTimestamp() == 0) {
            entity = service.getAlloc(tag);
            buffer.setUpdateTimestamp(System.currentTimeMillis());
            buffer.setStep(entity.getStep());
            buffer.setMinStep(entity.getStep());// leafAlloc中的step为DB中的step
        } else {
            long duration = System.currentTimeMillis() - buffer.getUpdateTimestamp();
            int nextStep = buffer.getStep();
            if (duration < SEGMENT_DURATION) {
                if (nextStep * 2 > MAX_STEP) {
                    // do nothing
                } else {
                    nextStep = nextStep * 2;
                }
            } else if (duration < SEGMENT_DURATION * 2) {
                // do nothing with nextStep
            } else {
                nextStep = nextStep / 2 >= buffer.getMinStep() ? nextStep / 2 : nextStep;
            }
            log.info("leafKey[{}], step[{}], duration[{}mins], nextStep[{}]", tag, buffer.getStep(), String.format("%.2f", ((double) duration / (1000 * 60))), nextStep);
            AllocEntity temp = new AllocEntity();
            temp.setTag(tag);
            temp.setStep(nextStep);
            entity = service.updateMaxIdByCustomAlloc(temp);
            buffer.setUpdateTimestamp(System.currentTimeMillis());
            buffer.setStep(nextStep);
            buffer.setMinStep(entity.getStep());// leafAlloc的step为DB中的step
        }
        // must set value before set max
        long value = entity.getMaxId() - buffer.getStep();
        segment.getValue().set(value);
        segment.setMax(entity.getMaxId());
        segment.setStep(buffer.getStep());
        // sw.stop();
        log.info("updateSegmentFromDb {}", tag + " " + segment);
    }

    public ID getIdFromSegmentBuffer(final SegmentBuffer buffer) {
        int retry = 5;
        while (true) {
            buffer.rLock().lock();
            try {
                final Segment segment = buffer.getCurrent();
                if (!buffer.isNextReady() && (segment.getIdle() < 0.9 * segment.getStep()) && buffer.getThreadRunning().compareAndSet(false, true)) {
                    threadPool.execute(() -> {
                        Segment next = buffer.getSegments()[buffer.nextPos()];
                        boolean updateOk = false;
                        try {
                            updateSegmentFromDB(buffer.getKey(), next);
                            updateOk = true;
                            log.info("update segment {} from db {}", buffer.getKey(), next);
                        } catch (Exception e) {
                            log.warn(buffer.getKey() + " updateSegmentFromDb exception", e);
                        } finally {
                            if (updateOk) {
                                buffer.wLock().lock();
                                buffer.setNextReady(true);
                                buffer.getThreadRunning().set(false);
                                buffer.wLock().unlock();
                            } else {
                                buffer.getThreadRunning().set(false);
                            }
                        }
                    });
                }
                long value = segment.getValue().getAndIncrement();
                if (value < segment.getMax()) {
                    return new ID(value, Status.SUCCESS);
                }
            } finally {
                buffer.rLock().unlock();
            }
            waitAndSleep(buffer);
            buffer.wLock().lock();
            try {
                final Segment segment = buffer.getCurrent();
                long value = segment.getValue().getAndIncrement();
                if (value < segment.getMax()) {
                    return new ID(value, Status.SUCCESS);
                }
                if (buffer.isNextReady()) {
                    buffer.switchPos();
                    buffer.setNextReady(false);
                } else {
                    log.error("Both two segments in {} are not ready!", buffer);
                    if (retry-- == 0) { // 重试
                        return new ID(EXCEPTION_ID_TWO_SEGMENTS_ARE_NULL, Status.UNUSUAL);
                    }
                }
            } finally {
                buffer.wLock().unlock();
            }
        }
    }

    private void waitAndSleep(SegmentBuffer buffer) {
        int roll = 0;
        while (buffer.getThreadRunning().get()) {
            roll += 1;
            if (roll > 10000) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    break;
                } catch (InterruptedException e) {
                    log.warn("Thread {} Interrupted", Thread.currentThread().getName());
                    break;
                }
            }
        }
    }
}
