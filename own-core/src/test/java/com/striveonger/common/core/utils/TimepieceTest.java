package com.striveonger.common.core.utils;

import junit.framework.TestCase;

public class TimepieceTest extends TestCase {

    public void testTimepiece() {
        Timepiece timepiece = Timepiece.of("XXX");
        SleepHelper.sleepSeconds(2);
        timepiece.keep("B");
        SleepHelper.sleepSeconds(3);
        timepiece.keep("C");
        SleepHelper.sleepSeconds(4);
        timepiece.keep("D");
        SleepHelper.sleepSeconds(5);
        timepiece.show();
    }

    public void test() {
        System.out.println("A:" + (-1L ^ (-1L << 10)));
        System.out.println("B:" + (~(-1L << 5)));
        System.out.println("C:" + (2 << 1));
    }
}