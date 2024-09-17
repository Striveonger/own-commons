package com.striveonger.common.core.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.striveonger.common.core.Jackson;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Lee
 * @since 2023-11-21 11:23
 */
public class LogJSONConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        Map<String, Object> log = new HashMap<>();
        log.put("timestamp", event.getTimeStamp());
        log.put("level", event.getLevel().toString());
        log.put("logger", event.getLoggerName());
        log.put("thread", event.getThreadName());

        // 填入MDC信息
        log.putAll(MDC.getCopyOfContextMap());
        // ERROR、REQUEST、OPERATION、NORMAL
        IThrowableProxy throwable = event.getThrowableProxy();
        Map<String, Object> content = new HashMap<>();
        if (throwable == null) {
            // 正常的日志输出
            log.put("type", "NORMAL");
            content.put("message", event.getFormattedMessage());
        } else {
            log.put("type", "ERROR");
            content.put("error_desc", throwable.getClassName());
            content.put("error_message", throwable.getMessage());
            content.put("error_stack_trace", getErrorStackTrace(throwable));
        }
        log.put("content", content);
        return Jackson.toJSONString(log) + "\n";
    }

    private String getErrorStackTrace(IThrowableProxy throwable) {
        StringBuilder result = new StringBuilder();
        result.append(throwable.getClassName()).append(":").append(throwable.getMessage()).append("\n");
        for (StackTraceElementProxy element : throwable.getStackTraceElementProxyArray()) {
            result.append(element.toString()).append("\n");
        }
        return result.toString();
    }

}

