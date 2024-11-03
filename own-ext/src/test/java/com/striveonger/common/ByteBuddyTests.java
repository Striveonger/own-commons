package com.striveonger.common;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author Mr.Lee
 * @since 2024-10-29 11:15
 */
public class ByteBuddyTests {

    @Test
    public void hello() throws Exception {
        Object o = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
        String helloWorld = o.toString();
        System.out.println(helloWorld);
    }

    @Test
    public void test() throws Exception {
        Class<? extends Xab> aClass = new ByteBuddy()
                .subclass(Xab.class)
                .method(ElementMatchers.named("hello"))
                .intercept(
                        MethodDelegation.to(HelloInterceptor.class)
                )
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();
        Xab xab = aClass.getDeclaredConstructor().newInstance();
        String x = xab.hello("abc");
        System.out.println(x);
    }

    public interface Xab {
        String hello(String x);
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

    public static class HelloInterceptor<T> {

        @RuntimeType
        public static String intercept() {
            // 这里添加你的自定义逻辑
            System.out.println("在拦截hello方法时执行自定义逻辑");
            return "XXX";
        }
    }

}
