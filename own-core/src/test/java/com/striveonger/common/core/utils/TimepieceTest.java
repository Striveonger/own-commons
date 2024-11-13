package com.striveonger.common.core.utils;

import com.striveonger.common.core.ThreadKit;
import com.striveonger.common.core.Timepiece;
import junit.framework.TestCase;

public class TimepieceTest extends TestCase {

    public void testTimepiece() {
        Timepiece timepiece = Timepiece.of("XXX");
        ThreadKit.sleepSeconds(2);
        timepiece.keep("B");
        ThreadKit.sleepSeconds(3);
        timepiece.keep("C");
        ThreadKit.sleepSeconds(4);
        timepiece.keep("D");
        ThreadKit.sleepSeconds(5);
        timepiece.show();
    }

    public void test() {
        System.out.println("A:" + (-1L ^ (-1L << 10)));
        System.out.println("B:" + (~(-1L << 5)));
        System.out.println("C:" + (2 << 1));
    }
}