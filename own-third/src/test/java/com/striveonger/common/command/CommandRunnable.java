package com.striveonger.common.command;

import cn.hutool.core.io.IoUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * @author Mr.Lee
 * @since 2024-08-26 14:51
 */
public class CommandRunnable {
    private final Logger log = LoggerFactory.getLogger(CommandRunnable.class);

    private final  ExecutorService executorService = Executors.newCachedThreadPool();

    @Test
    public void test() throws Exception {
        Process process = Runtime.getRuntime().exec("pwd");
        System.out.println(process);

        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        Future<?> future = executorService.submit(streamGobbler);

        int exitCode = process.waitFor();
        Object o = future.get();
        // System.out.println(o);
        assertEquals(0, exitCode);
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    @Test
    public void test2() throws Exception {
        Process p = new ProcessBuilder("helm", "list", "-n", "omm").start();
        int exitCode = p.waitFor();
        System.out.println("exitCode: " + exitCode);
        byte[] bytes = IoUtil.readBytes(p.getInputStream(), true);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }

}
