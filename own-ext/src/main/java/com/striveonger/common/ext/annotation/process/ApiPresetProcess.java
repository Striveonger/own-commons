package com.striveonger.common.ext.annotation.process;

import com.striveonger.common.core.loader.OwnClassLoader;
import com.striveonger.common.ext.annotation.ApiPreset;
import com.striveonger.common.ext.annotation.Scanner;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.any;

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
