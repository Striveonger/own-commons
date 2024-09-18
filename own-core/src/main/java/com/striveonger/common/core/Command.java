package com.striveonger.common.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.striveonger.common.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 命令执行器
 * @author Mr.Lee
 * @since 2024-09-17 10:03
 */
public class Command {
    private static final Logger log = LoggerFactory.getLogger(Command.class);

    private String home;

    private final List<String> cmds = new ArrayList<>();

    private final Map<String, String> env = new HashMap<>();

    private Result result;

    private Command() {}

    public static Command of(String ...cmds) {
        Command command = new Command();
        command.cmds.addAll(Arrays.stream(cmds).flatMap(x -> StrUtil.split(x, " ").stream()).toList());
        return command;
    }

    /**
     * 设置工作目录
     * @param home
     * @return
     */
    public Command home(String home) {
        this.home = home;
        return this;
    }

    /**
     * 设置环境变量
     * @param key
     * @param value
     * @return
     */
    public Command env(String key, String value) {
        env.put(key, value);
        return this;
    }

    public Result run() {
        // 1. 校验并构造参数
        if (CollUtil.isEmpty(cmds)) {
            throw CustomException.of("commands is empty");
        }
        File dir = null;
        if (StrUtil.isNotBlank(this.home)) {
            dir = new File(this.home);
            if (!dir.exists()) {
                throw CustomException.of("home is not exist");
            }
            if (!dir.isDirectory()) {
                throw CustomException.of("home is not dir");
            }
        }

        // 2. 执行命令
        try {
            log.info("执行命令: {}", StrUtil.join(" ", cmds));
            ProcessBuilder builder = new ProcessBuilder(cmds);
            builder.directory(dir);
            builder.environment().putAll(env);
            Process process = builder.start();
            this.result = new Result(Status.RUNNING, process, null);
        } catch (IOException e) {
            log.error("命令执行失败", e);
            this.result = new Result(Status.FAIL, null, "命令执行失败");
        }
        return this.result;
    }

    public static class Result {
        private Status status;
        private List<String> lines;
        private Thread thread;

        public Result(Status status, Process process, String content) {
            this.status = status;
            this.lines = StrUtil.isNotBlank(content) ? List.of(content) : List.of();
            if (Status.RUNNING == status && null != process) {
                this.thread = ThreadHelper.run(() -> {
                    try {
                        int exitCode = process.waitFor(); // 阻塞方法
                        if (exitCode == 0) {
                            this.status = Status.SUCCESS;
                        } else {
                            this.status = Status.FAIL;
                        }
                        InputStream in = null;
                        try {
                            in = process.getInputStream();
                            this.lines = IoUtil.readLines(in, UTF_8, new ArrayList<>());
                        } finally {
                            IoUtil.close(in);
                            process.destroy();
                        }
                    } catch (Exception e) {
                        log.error("命令执行失败", e);
                    }
                });
            }
        }

        public Status getStatus() {
            return status;
        }

        public List<String> getLines() {
            return getLines(true);
        }

        /**
         * 获取命令执行结果
         *
         * @param sync 是否要同步等待返回结果
         * @return
         */
        public List<String> getLines(boolean sync) {
            if (this.thread.isAlive() && sync && Status.RUNNING == this.status) {
                ThreadHelper.join(this.thread);
            }
            return this.lines == null ? List.of() : this.lines;
        }

        public String getContent() {
            return getContent(true);
        }

        public String getContent(boolean sync) {
            return String.join("\n", getLines(sync));
        }
    }

    public enum Status {
        SUCCESS, FAIL, RUNNING
    }
}
