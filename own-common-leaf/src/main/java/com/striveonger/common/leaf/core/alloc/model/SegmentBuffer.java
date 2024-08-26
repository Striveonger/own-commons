package com.striveonger.common.leaf.core.alloc.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Mr.Lee
 * @since 2024-08-26 20:49
 */
public class SegmentBuffer {
    private String key;
    private final Segment[] segments; // 双buffer
    private volatile int currentPos; // 当前的使用的segment的index
    private volatile boolean nextReady; // 下一个segment是否处于可切换状态
    private volatile boolean initOk; // 是否初始化完成
    private final AtomicBoolean threadRunning; // 线程是否在运行中
    private final ReadWriteLock lock;

    private volatile int step;
    private volatile int minStep;
    private volatile long updateTimestamp;

    public SegmentBuffer() {
        segments = new Segment[]{new Segment(this), new Segment(this)};
        currentPos = 0;
        nextReady = false;
        initOk = false;
        threadRunning = new AtomicBoolean(false);
        lock = new ReentrantReadWriteLock();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Segment[] getSegments() {
        return segments;
    }

    public Segment getCurrent() {
        return segments[currentPos];
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public int nextPos() {
        return (currentPos + 1) % 2;
    }

    public void switchPos() {
        currentPos = nextPos();
    }

    public boolean isInitOk() {
        return initOk;
    }

    public void setInitOk(boolean initOk) {
        this.initOk = initOk;
    }

    public boolean isNextReady() {
        return nextReady;
    }

    public void setNextReady(boolean nextReady) {
        this.nextReady = nextReady;
    }

    public AtomicBoolean getThreadRunning() {
        return threadRunning;
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getMinStep() {
        return minStep;
    }

    public void setMinStep(int minStep) {
        this.minStep = minStep;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("SegmentBuffer{");
        result.append("key='").append(key).append('\'');
        result.append(", segments=").append(Arrays.toString(segments));
        result.append(", currentPos=").append(currentPos);
        result.append(", nextReady=").append(nextReady);
        result.append(", initOk=").append(initOk);
        result.append(", threadRunning=").append(threadRunning);
        result.append(", step=").append(step);
        result.append(", minStep=").append(minStep);
        result.append(", updateTimestamp=").append(updateTimestamp);
        result.append('}');
        return result.toString();
    }
}
