import com.striveonger.common.core.Jackson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.function.FunctionToolCallback;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author Mr.Lee
 * @since 2025-04-09 18:00
 */
public class TestTools {
    private final Logger log = LoggerFactory.getLogger(TestTools.class);

    private OllamaChatModel model;

    @BeforeEach
    public void setUp() {
        model = OllamaChatModel
                .builder()
                .ollamaApi(new OllamaApi("http://localhost:11434"))
                .defaultOptions(OllamaOptions
                        .builder()
                        // .model("gemma3:1b")
                        .model("llama3.1:8b")
                        // .model("qwq")
                        .temperature(0.95) // 思维活跃度
                        .build()
                ).build();
    }

    @Test
    public void testCallback() throws Exception {
        InputStream in = new FileInputStream("/Users/striveonger/development/workspace/favor/own-commons/own-ai/own-llm/src/test/resources/changelog.json");
        String changelog = Jackson.builder().read(in).build().toString();

        List<String> messages = List.of(
                "你现在的角色是DBA",
                "现维护的MySQL库中有四张数据表, 以下是用Liquibase的JSON格式描述的表结构 \n" + changelog
        );

        FunctionToolCallback<MockDataQueryService.Request, MockDataQueryService.Response> callback = FunctionToolCallback
                .builder("generateSQL", new MockDataQueryService())
                .description("给出可执行的SQL查询语句")
                .inputType(MockDataQueryService.Request.class)
                .build();
        Prompt prompt = new Prompt(messages.stream().map(UserMessage::new).map(Message.class::cast).toList());
        ChatClient.CallResponseSpec response = ChatClient
                .create(model)
                .prompt(prompt)
                .user("我现在想知道每天晚上10点之后, 还在购物的用户年龄范围")
                .tools(callback).call();
        String content = response.content();
        log.info("llm response content: {}", content);

    }

}
