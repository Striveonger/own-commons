package com.striveonger.common.ext.annotation.process;

import com.striveonger.common.ext.annotation.ApiPreset;
import com.striveonger.common.ext.annotation.Scanner;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.any;

/**
 * 添加预设的处理程序
 * @author Mr.Lee
 * @since 2024-10-27 16:17
 */
public class ApiPresetProcess {
    private static final Logger log = LoggerFactory.getLogger(ApiPresetProcess.class);

    public static void process(String basePackage) {
        log.info("ApiPresetProcess process");
        ByteBuddyAgent.install();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        List<Class<?>> list = Scanner.of().list(basePackage, ApiPreset.class);
        for (Class<?> clazz : list) {
            // 使用Byte Buddy重新定义类
            DynamicType.Builder<?> builder = new ByteBuddy().redefine(clazz).method(any()).intercept(MethodDelegation.to(NewMethodInterceptor.class));
            try(DynamicType.Unloaded<?> unloaded = builder.make()) {
                unloaded.load(loader, ClassLoadingStrategy.Default.WRAPPER).getLoaded();
            }
        }
    }

    public static class NewMethodInterceptor {

        @RuntimeType
        public static Object intercept(@SuperCall Callable<?> callable, @Origin Method method) throws Exception {
            if ("hello".equals(method.getName())) {
                // 这里是新添加方法的具体逻辑
                System.out.println("新添加的方法被调用，执行特定逻辑");
                return null;
            }
            return callable.call();
        }
    }
}
