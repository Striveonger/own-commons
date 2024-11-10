package com.striveonger.common.ext.annotation.process;

import com.striveonger.common.ext.annotation.ApiPreset;
import com.striveonger.common.ext.annotation.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 添加预设的处理程序
 *
 * @author Mr.Lee
 * @since 2024-10-27 16:17
 */
public class ApiPresetProcess {
    private static final Logger log = LoggerFactory.getLogger(ApiPresetProcess.class);

    static {
        process("com.striveonger.common");
    }


    public static void process(String basePackage) {
        log.info("ApiPresetProcess process");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        List<Class<?>> list = Scanner.of().list(basePackage, ApiPreset.class);
        for (Class<?> clazz : list) {
            System.out.println(clazz.getName());
        }
    }


}
