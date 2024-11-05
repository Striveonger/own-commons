package com.striveonger.common.core.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.Lee
 * @since 2024-11-03 21:51
 */
public class OwnClassLoader extends ClassLoader {
    private final Logger log = LoggerFactory.getLogger(OwnClassLoader.class);

    private final static Map<String, Class<?>> map = new ConcurrentHashMap<>();

    public OwnClassLoader(ClassLoader parent) {
        super(parent);
    }

    public static void register(String name, Class<?> clazz) {
        map.put(name, clazz);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz;
        if (map.containsKey(name)) {
            clazz = map.get(name);
        } else {
            clazz = super.loadClass(name, resolve);
        }

        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }
}
