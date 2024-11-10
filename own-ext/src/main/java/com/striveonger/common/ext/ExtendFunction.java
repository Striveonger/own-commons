package com.striveonger.common.ext;

import com.striveonger.common.core.loader.OwnClassLoader;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过修改字节码的方式, 动态修改指定方法
 * @author Mr.Lee
 * @since 2024-11-10 14:07
 */
public class ExtendFunction {
    private final Logger log = LoggerFactory.getLogger(ExtendFunction.class);

    public static void ext(Class<?> clazz, String method, Object target) {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        ClassLoader loader = new OwnClassLoader(current);
        DynamicType.Builder<?> builder = new ByteBuddy().subclass(clazz).method(ElementMatchers.named(method)).intercept(MethodDelegation.to(target));
        Class<?> dynamicClass = builder.make().load(loader).getLoaded();
        OwnClassLoader.register(clazz.getName(), dynamicClass);
        Thread.currentThread().setContextClassLoader(loader);
    }

}
