package com.striveonger.common.leaf.core.snowflake;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.lang.Assert;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.leaf.core.ID;
import com.striveonger.common.leaf.core.IDGen;
import com.striveonger.common.leaf.core.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;

import static com.striveonger.common.core.constant.ResultStatus.NON_SUPPORT;
import static com.striveonger.common.leaf.core.Status.SUCCESS;
import static com.striveonger.common.leaf.core.Status.UNUSUAL;

/**
 * 雪花算法, 生成ID <br/>
 * <p>
 * Twitter的Snowflake 算法<br>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
 *
 * <p>
 * snowflake的结构如下(每部分用-分开):<br>
 *
 * <pre>
 * 符号位（1bit）- 时间戳相对值（41bit）- 机器标志（10bit）- 递增序号（12bit）
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 0000000000 - 000000000000
 * </pre>
 * <p>
 * 第一位为未使用(符号位表示正数)，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是10位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 *
 * @author Mr.Lee
 * @since 2024-09-01 18:39
 */
public class SnowflakeIDGen implements IDGen {
    private final Logger log = LoggerFactory.getLogger(SnowflakeIDGen.class);

    // 参考: https://mp.weixin.qq.com/s/MJXEJsA8A8AdzQ-MDhhNAg

    /**
     * 随机数
     */
    private static final Random RANDOM = new Random();

    /**
     * 默认的起始时间, 为 2016-07-20 13:14:05+08:00
     */
    public static long DEFAULT_TWEPOCH = 1468991645688L;

    /**
     * 默认回拨时间, 500毫秒
     */
    public static long DEFAULT_TIME_OFFSET = 500L;

    /**
     * 机器节点占10位
     */
    private static final int WORKER_ID_BITS = 10;

    /**
     * 最大支持机器节点数0~1023，一共1024个
     */
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    public static final int MAX_WORKER_ID = -1 ^ (-1 << WORKER_ID_BITS);

    /**
     * 序列号12位（表示只允许workId的范围为：0-4095）
     */
    private static final int SEQUENCE_BITS = 12;

    /**
     * 机器节点左移12位
     */
    private static final int WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 时间毫秒数左移22位
     */
    private static final int TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 序列掩码，用于限定序列最大值不能超过4095
     */
    private static final int SEQUENCE_MASK = ~(-1 << SEQUENCE_BITS);


    /**
     * 初始化时间点
     */
    private final long twepoch;
    /**
     * 终端ID
     */
    private final long workerId;

    /**
     * 允许的时钟回拨毫秒数
     */
    private final long timeOffset;

    /**
     * 当在低频模式下时，序号始终为0，导致生成ID始终为偶数<br>
     * 此属性用于限定一个随机上限，在不同毫秒下生成序号时，给定一个随机数，避免偶数问题。<br>
     * 注意次数必须小于{@link #SEQUENCE_MASK}，{@code 0}表示不使用随机数。<br>
     * 这个上限不包括值本身。
     */
    private final int randomSequenceLimit;

    /**
     * 自增序号，当高频模式下时，同一毫秒内生成N个ID，则这个序号在同一毫秒下，自增以避免ID重复。
     */
    private long sequence = 0L;
    private long lastTimestamp = -1L;


    /**
     * 随机构造一个工作机器节点ID
     */
    public SnowflakeIDGen() {
        this(RANDOM.nextInt(MAX_WORKER_ID));
    }

    /**
     * 创建雪花算法的IDGen
     * @param workerId 工作机器节点ID
     */
    public SnowflakeIDGen(long workerId) {
        this(DEFAULT_TWEPOCH, workerId, DEFAULT_TIME_OFFSET, 99);
    }

    /**
     * @param epochDate           初始化时间起点(null表示默认起始日期), 后期修改会导致id重复, 如果要修改连workerId dataCenterId, 慎用
     * @param workerId            工作机器节点ID
     * @param timeOffset          允许时间回拨的毫秒数
     * @param randomSequenceLimit 限定一个随机上限, 在不同毫秒下生成序号时, 给定一个随机数, 避免偶数问题, 0表示无随机, 上限不包括值本身
     */
    public SnowflakeIDGen(Long epochDate, long workerId, long timeOffset, int randomSequenceLimit) {
        this.twepoch = (null == epochDate) || epochDate <= 0 ? DEFAULT_TWEPOCH : epochDate;
        this.workerId = Assert.checkBetween(workerId, 0, MAX_WORKER_ID, () -> CustomException.of(NON_SUPPORT));
        this.timeOffset = timeOffset;
        this.randomSequenceLimit = Assert.checkBetween(randomSequenceLimit, 0, SEQUENCE_MASK, () -> CustomException.of(NON_SUPPORT));
    }

    @Override
    public ID next() {
        synchronized (this) {
            long timestamp = timeGen();
            // 防止系统时间回调, 导致ID冲突的情况
            if (timestamp < lastTimestamp) {
                long offset = lastTimestamp - timestamp;
                if (offset <= timeOffset) {
                    // 等待
                    try {
                        wait(offset << 1);
                        timestamp = timeGen();
                        if (timestamp < lastTimestamp) {
                            // 等待后, 重新获取的时间, 依旧小于上次的时间, 则返回错误
                            return new ID(-1, UNUSUAL);
                        }
                    } catch (InterruptedException e) {
                        // 等待被中断, 返回错误
                        log.error("wait interrupted");
                        return new ID(-2, UNUSUAL);
                    }
                } else {
                    // 超过系统可容忍的最大可回播时间, 返回错误
                    return new ID(-3, UNUSUAL);
                }
            }
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & SEQUENCE_MASK;
                if (sequence == 0) {
                    // 序列号码耗尽, 等待下一毫秒
                    timestamp = tilNextMillis(lastTimestamp);
                    sequence = RANDOM.nextInt(randomSequenceLimit);
                }
            } else {
                //如果是新的ms开始
                sequence = RANDOM.nextInt(randomSequenceLimit);
            }
            lastTimestamp = timestamp;
            long id = ((timestamp - twepoch) << TIMESTAMP_LEFT_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;
            return new ID(id, SUCCESS);
        }
    }

    @Override
    public ID next(String tag) {
        throw new CustomException(NON_SUPPORT, "不支持该方法");
    }

    /**
     * 获取系统时间戳
     *
     * @return
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 直到下一毫秒
     *
     * @param lastTimestamp 上一次生成ID的时间戳
     * @return 返回下一毫秒的时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }
}
