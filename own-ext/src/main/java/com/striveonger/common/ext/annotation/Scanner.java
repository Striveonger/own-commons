package com.striveonger.common.ext.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Mr.Lee
 * @since 2024-10-27 16:33
 */
public class Scanner {
    private final Logger log = LoggerFactory.getLogger(Scanner.class);

    private Scanner() {}

    public static Scanner of() {
        return new Scanner();
    }

    /**
     * 检索出被指定注解标记的类
     * @param packageName
     * @param annotationClass
     * @return
     */
    public List<Class<?>> list(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> list = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = loader.getResources(packageName.replace(".", "/"));
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if ("file".equals(resource.getProtocol())) {
                    File directory = new File(resource.getFile());
                    findClassesWithAnnotation(directory, packageName, annotationClass, list);
                }

            }

        } catch (IOException e) {
            log.error("", e);
        }
        return list;
    }

    private void findClassesWithAnnotation(File directory, String packageName, Class<? extends Annotation> annotationClass, List<Class<?>> list) {
        if (!directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        if (files!= null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findClassesWithAnnotation(file, packageName + "." + file.getName(), annotationClass, list);
                } else if (file.getName().endsWith(".class")) {
                    try {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(annotationClass)) {
                            list.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
