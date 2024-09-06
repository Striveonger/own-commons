package com.striveonger.common.leaf.core;

import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.leaf.config.LeafConfig;
import com.striveonger.common.leaf.core.segment.SegmentIDGen;
import com.striveonger.common.leaf.core.snowflake.SnowflakeIDGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Mr.Lee
 * @since 2024-08-27 23:51
 */
public class FitIDGen implements IDGen {
    private final Logger log = LoggerFactory.getLogger(FitIDGen.class);

    private final SegmentIDGen segment;
    private final SnowflakeIDGen snowflake;


    public FitIDGen(SegmentIDGen segment, SnowflakeIDGen snowflake) {
        this.segment = segment;
        this.snowflake = Objects.requireNonNullElseGet(snowflake, SnowflakeIDGen::new);
    }

    @Override
    public ID next() {
        return snowflake.next();
    }

    @Override
    public ID next(String tag) {
        if (segment == null) {
            throw new CustomException(ResultStatus.NON_SUPPORT, "不支持号段分配模式");
        }
        return segment.next(tag);
    }
}
