package com.striveonger.common.core;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class RandomKitTest {

    private static final Logger log = LoggerFactory.getLogger(RandomKitTest.class);

    @Test
    void randomString() {
        String s = RandomKit.randomString(6);
        log.info(s);
    }

    @Test
    void testRandomString() {
    }

    @Test
    void randomInt() {
    }

    @Test
    void testRandomInt() {
    }
}