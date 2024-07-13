package com.striveonger.common.core.log;

import cn.hutool.core.lang.UUID;
import org.slf4j.MDC;

public class InitMDC {

    public static void start() {
        MDC.put("traceId", UUID.fastUUID().toString());
    }
}
