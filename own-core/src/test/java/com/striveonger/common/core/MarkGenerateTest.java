package com.striveonger.common.core;

import org.junit.jupiter.api.Test;

public class MarkGenerateTest {

    @Test
    public void test() {
        String mark = MarkGenerate.build("xx");
        System.out.println(mark);
        System.out.println("8cb5faa6914143d9b11166c92469e0b0");
    }
}