package com.striveonger.common.core.utils;


import cn.hutool.core.lang.Dict;
import org.junit.Test;

public class JacksonTest {


    @Test
    public void test() {
        System.out.println(JacksonUtils.toJSONString(Dict.of("a", 1, "b", 2)));
    }
}