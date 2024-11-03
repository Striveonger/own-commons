package com.striveonger.common;

import com.striveonger.common.ext.annotation.V3;
import com.striveonger.common.ext.annotation.aspect.TimepieceAspect;
import com.striveonger.common.ext.annotation.process.ApiPresetProcess;
import com.striveonger.common.ext.config.ExtAutoConfiguration;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Mr.Lee
 * @since 2024-10-27 12:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExtAutoConfiguration.class, TimepieceAspect.class, V1.class, V2.class, V3.class})
public class IntegraTest {
    private final Logger log = LoggerFactory.getLogger(IntegraTest.class);

    @Resource
    V1 v1;

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.println("Current Thread ClassLoader: " + loader);
        ApiPresetProcess.process("com.striveonger.common");
    }

    @Test
    public void test() {
        v1.run();
    }


}
