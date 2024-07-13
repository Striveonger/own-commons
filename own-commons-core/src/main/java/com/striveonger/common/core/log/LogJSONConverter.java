package com.cecbrain.omm.core.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import com.alibaba.fastjson2.JSONObject;
import com.cecbrain.omm.core.constant.CommonRespStatus;
import com.cecbrain.omm.core.exception.CustomIllegalStateException;
import org.slf4j.MDC;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-11-21 11:23
 */
public class LogJSONConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        JSONObject log = new JSONObject();
        log.put("timestamp", event.getTimeStamp());
        log.put("level", event.getLevel().toString());
        log.put("logger", event.getLoggerName());
        log.put("thread", event.getThreadName());

        // 填入MDC信息
        log.putAll(MDC.getCopyOfContextMap());
        // ERROR、REQUEST、OPERATION、NORMAL
        IThrowableProxy throwable = event.getThrowableProxy();
        JSONObject content = new JSONObject();
        if (throwable == null) {
            // 正常的日志输出
            log.put("type", "NORMAL");
            log.put("public_code", CommonRespStatus.LOG_NORMAL_OUTPUT.status());
            content.put("message", event.getFormattedMessage());
        } else {
            log.put("type", "ERROR");
            if (throwable instanceof CustomIllegalStateException e) {
                log.put("public_code", e.getCode());
            } else {
                log.put("public_code", CommonRespStatus.SERVICE_NOT_AVAILABLE.status());
            }
            content.put("error_desc", throwable.getClassName());
            content.put("error_message", throwable.getMessage());
            content.put("error_stack_trace", getErrorStackTrace(throwable));
        }
        log.put("content", content);
        return log + "\n";
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

