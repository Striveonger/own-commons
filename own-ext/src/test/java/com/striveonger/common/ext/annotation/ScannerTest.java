package com.striveonger.common.ext.annotation;

import org.junit.Test;

import java.util.List;

public class ScannerTest {

    @Test
    public void test() {
        List<Class<?>> list = Scanner.of().list("com.striveonger.common", ApiPreset.class);
        System.out.println(list);
    }
}