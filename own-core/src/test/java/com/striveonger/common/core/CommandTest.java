package com.striveonger.common.core;

import org.junit.Test;

public class CommandTest {

    @Test
    public void test() {
        Command.Result result = Command.of("ls", "-lh").home("/Users/striveonger").run();
        // Command.Result result = Command.of("printenv XYZ").env("XYZ", "1").run();
        // Command.Result result = Command.of("ping", "10.13.144.9", "-c", "4").run();
        String content = result.getContent();
        System.out.println(result.getStatus());
        System.out.println(content); // 命令还在执行中, 返回值为 ""

        if (result.getStatus() == Command.Status.RUNNING) {
            ThreadHelper.sleepSeconds(5);
        }
        // System.out.println(result.getStatus());
        // System.out.println(result.getContent(false));

    }
}