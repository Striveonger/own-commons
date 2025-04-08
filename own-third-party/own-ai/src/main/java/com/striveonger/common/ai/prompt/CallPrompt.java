package com.striveonger.common.ai.prompt;

import cn.hutool.core.collection.CollUtil;
import com.agentsflex.core.llm.functions.Function;
import com.agentsflex.core.message.HumanMessage;
import com.agentsflex.core.message.Message;
import com.agentsflex.core.message.SystemMessage;
import com.agentsflex.core.prompt.Prompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mr.Lee
 * @since 2025-04-01 16:01
 */
public class CallPrompt extends Prompt {
    private final Logger log = LoggerFactory.getLogger(CallPrompt.class);

    private SystemMessage systemMessage;

    private HumanMessage humanMessage;

    private boolean autoCall = true;

    private final List<HumanMessage> historyMessages = new ArrayList<>();

    public CallPrompt(String message, Class<?> functionsClass) {
        this.humanMessage = new HumanMessage(message);
        this.humanMessage.addFunctions(functionsClass);
    }

    public CallPrompt(String message, List<Function> functions) {
        this.humanMessage = new HumanMessage(message);
        this.humanMessage.addFunctions(functions);
    }

    public SystemMessage getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(SystemMessage systemMessage) {
        this.systemMessage = systemMessage;
    }

    public HumanMessage getHumanMessage() {
        return humanMessage;
    }

    public void setHumanMessage(HumanMessage humanMessage) {
        this.humanMessage = humanMessage;
    }

    public boolean isAutoCall() {
        return autoCall;
    }

    public void setAutoCall(boolean autoCall) {
        this.autoCall = autoCall;
    }

    public void setHistoryMessages(List<HumanMessage> historyMessages) {
        this.historyMessages.addAll(historyMessages);
    }

    @Override
    public List<Message> toMessages() {
        List<Message> messages = new ArrayList<>();
        if (Objects.nonNull(systemMessage)) {
            messages.add(systemMessage);
        }
        if (CollUtil.isNotEmpty(historyMessages)) {
            messages.addAll(historyMessages);
        }
        messages.add(humanMessage);
        return messages;
    }
}
