package com.striveonger.common.ext.annotation.process;

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

    public static void process(String basePackage) {
        log.info("ApiPresetProcess process");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        log.info("loader: {}", loader.getName());
        List<Class<?>> list = Scanner.of().list(basePackage, ApiPreset.class);
        for (Class<?> clazz : list) {
            HelloInterceptor interceptor = new HelloInterceptor(clazz.getSimpleName());
            // 使用Byte Buddy重新定义类
            DynamicType.Builder<?> builder = new ByteBuddy().redefine(clazz).method(any()).intercept(MethodDelegation.to(HelloInterceptor.class));
            // DynamicType.Builder<?> builder = new ByteBuddy().subclass(clazz).method("hello").intercept(MethodDelegation.to(interceptor));
            try (DynamicType.Unloaded<?> unloaded = builder.make()) {
                // 先卸载旧类
                // 加载新定义的类
                Class<?> newClass = unloaded.load(loader).getLoaded();
                Object o = newClass.getDeclaredConstructor().newInstance();
                Method hello = newClass.getMethod("helloX", String.class);
                Object object = hello.invoke(o, clazz.toGenericString());
                System.out.println(object);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    public static class HelloInterceptor {

        private final String title;

        public HelloInterceptor(String title) {
            this.title = title;
        }

        @RuntimeType
        public Object intercept(@Origin Method method, @AllArguments Object[] args, @SuperCall Callable<?> callable) throws Exception {
            System.out.println(callable.getClass().getName());
            callable.call();
            return "Hello " + title;
        }

    }
}
