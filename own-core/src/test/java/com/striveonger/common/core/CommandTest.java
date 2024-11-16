package com.striveonger.common.core;

import org.junit.Test;

import java.util.List;

public class CommandTest {

    @Test
    public void test() {
        List<String> cmds = List.of("ping", "127.0.0.1", "-c", "4");
        Command.Result result = Command.of(cmds).console(System.out::println).run();
        result.await();

        cmds = List.of("bash", "-c", "ping 127.0.0.1 -c 4");
        result = Command.of(cmds, false).console(System.out::println).run();
        result.await();
    }
}