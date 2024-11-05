package com.striveonger.common.loader;

import com.striveonger.common.core.Jackson;
import com.striveonger.common.core.loader.OwnClassLoader;
import com.striveonger.common.core.result.Result;
import com.striveonger.common.ext.annotation.process.ApiPresetProcess;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.any;

/**
 * @author Mr.Lee
 * @since 2024-11-05 11:24
 */

public class ClassLoaderTest {

    private OwnClassLoader loader;

    @Before
    public void init() {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        loader = new OwnClassLoader(current);
        Thread.currentThread().setContextClassLoader(loader);
        HelloInterceptor interceptor = new HelloInterceptor("Hello World!");
        DynamicType.Builder<?> builder = new ByteBuddy().subclass(HelloTemplate.class).method(any()).intercept(MethodDelegation.to(interceptor));
        Class<?> dynamicClass = builder.make().load(loader).getLoaded();
        OwnClassLoader.register(HelloTemplate.class.getName(), dynamicClass);
    }

    @Test
    public void test() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Mr.Lee");
        Class<?> aClass = loader.loadClass(HelloTemplate.class.getName());
        HelloTemplate template = (HelloTemplate) aClass.getDeclaredConstructor().newInstance();
        Result result = template.list(map);
        System.out.println(Jackson.toJSONString(result));
    }

    public static class HelloInterceptor {

        private final String title;

        public HelloInterceptor(String title) {
            this.title = title;
        }

        @RuntimeType
        public Object intercept(@Origin Method method, @AllArguments Object[] args, @SuperCall Callable<?> callable) throws Exception {
            if (args[0] instanceof Map map) {
                map.put("title", title);
                return Result.success().data(map);
            }
            return Result.success().data(Map.of("title", title));
        }

    }
}
