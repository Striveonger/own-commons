package com.striveonger.common.core;

import com.striveonger.common.core.thread.ThreadKit;
import junit.framework.TestCase;

public class TimepieceTest extends TestCase {

    public void testTimepiece() {
        Timepiece timepiece = Timepiece.of("XXX");
        ThreadKit.sleep(2000);
        timepiece.keep("B");
        ThreadKit.sleep(3000);
        timepiece.keep("C");
        ThreadKit.sleep(4000);
        timepiece.keep("D");
        ThreadKit.sleep(5000);
        timepiece.show();
    }

    public void test() {
        System.out.println("A:" + (-1L ^ (-1L << 10)));
        System.out.println("B:" + (~(-1L << 5)));
        System.out.println("C:" + (2 << 1));
    }
}