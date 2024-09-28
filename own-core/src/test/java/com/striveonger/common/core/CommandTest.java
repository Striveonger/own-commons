package com.striveonger.common.core;

import org.junit.Test;

import java.util.List;

public class CommandTest {

    @Test
    public void test() {
        List<String> cmds = List.of("ping", "127.0.0.1", "-c", "10");
        Command.Result result = Command.of(cmds).console(System.out::println).run();
        result.await();
    }
}