package com.striveonger.common.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.thread.ThreadKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

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

    private Consumer<Status> callback;
    private Consumer<String> console;

    private Command() {}

    public static Command of(List<String> cmds) {
        return of(cmds, true);
    }

    public static Command of(List<String> cmds, boolean split) {
        Command command = new Command();
        if (split) {
            command.cmds.addAll(cmds.stream().flatMap(x -> StrUtil.split(x, " ").stream()).toList());
        } else {
            command.cmds.addAll(cmds);
        }
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

    /**
     * 设置回调
     *
     * @param callback 回调
     * @return
     */
    public Command callback(Consumer<Status> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 设置控制台输出
     *
     * @param console
     * @return
     */
    public Command console(Consumer<String> console) {
        this.console = console;
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
            this.result = new Result(Status.RUNNING, process, callback, console);
        } catch (IOException e) {
            log.error("命令执行失败", e);
            this.result = new Result(Status.FAIL, null, callback, null);
        }
        return this.result;
    }

    public Result getResult() {
        return result;
    }

    public static class Result {
        private Status status;
        private final List<String> lines;
        private Thread outputThread = null;
        private Thread executeThread = null;
        private final Consumer<Status> callback;
        private final Consumer<String> console;

        public Result(Status status, Process process, Consumer<Status> callback, Consumer<String> console) {
            this.status = status;
            this.lines = new LinkedList<>();
            this.callback = callback;
            this.console = console;
            if (Status.RUNNING == status && null != process) {
                InputStream inputStream = process.getInputStream();
                this.outputThread = ThreadKit.run(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            lines.add(line);
                            execoutput(line);
                        }
                    } catch (IOException e) {
                        log.error("Error reading process output", e);
                    }
                });

                this.executeThread = ThreadKit.run(() -> {
                    try {
                        int exitCode = process.waitFor();
                        if (exitCode == 0) {
                            this.status = Status.SUCCESS;
                        } else {
                            this.status = Status.FAIL;
                        }
                        execallbakk();
                    } catch (Exception e) {
                        log.error("命令执行失败", e);
                    } finally {
                        IoUtil.close(inputStream);
                        process.destroy();
                    }
                });
            } else if (Status.FAIL == status || null == process) {
                this.lines.add("命令执行失败");
                this.status = Status.FAIL;
                execallbakk();
            }
        }

        public Status getStatus() {
            return status;
        }

        public List<String> getLines() {
            return new ArrayList<>(lines);
        }

        public String getContent() {
            return String.join("\n", getLines());
        }

        /**
         * 等待命令执行结束
         */
        public void await() {
            ThreadKit.join(executeThread);
            ThreadKit.join(outputThread);
        }

        private void execallbakk() {
            if (this.callback != null) {
                this.callback.accept(this.status);
            }
        }

        private void execoutput(String line) {
            if (this.console != null) {
                this.console.accept(line);
            }
        }
    }

    public enum Status {
        SUCCESS, FAIL, RUNNING
    }
}
