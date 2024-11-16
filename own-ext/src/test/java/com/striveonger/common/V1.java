package com.striveonger.common;

import com.striveonger.common.core.ThreadKit;
import com.striveonger.common.ext.annotation.Timepiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Mr.Lee
 * @since 2024-10-27 12:38
 */
@Component
public class V1 {
    private final Logger log = LoggerFactory.getLogger(V1.class);

    @Timepiece
    public void run() {
        for (int i = 0; i < 5; i++) {
            log.info("V1 {}", i);
            ThreadKit.sleep(300);
        }
    }
}
