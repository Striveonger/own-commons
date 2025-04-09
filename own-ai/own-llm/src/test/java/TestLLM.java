import cn.hutool.core.util.StrUtil;
import com.striveonger.common.core.thread.ThreadKit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Lee
 * @since 2025-04-08 17:51
 */
public class TestLLM {
    private final Logger log = LoggerFactory.getLogger(TestLLM.class);

    private OllamaChatModel model;

    @BeforeEach
    public void setUp() {
        // String s = "gemma3:1b";
        // String s = "llama3.1:8b";
        String s = "deepseek-r1:14b";

        model = OllamaChatModel
                .builder()
                .ollamaApi(new OllamaApi("http://localhost:11434"))
                .defaultOptions(OllamaOptions
                        .builder()
                        .model(s)
                        .temperature(0.95) // 思维活跃度
                        .build()
                ).build();
    }

    @Test
    public void testHello() {
        log.info("hello");
        Prompt prompt = new Prompt(List.of(new UserMessage("你好")));
        ChatResponse response = model.call(prompt);
        log.info(response.getResult().getOutput().getText());
    }

    @Test
    public void testAsync() {
        var ref = new Object() {
            long start = 0L;
            String id = "";
        };
        List<String> messages = List.of(
                "你是一个运维开发工程师, 你需要根据以下规则来回答用户的问题",
                "k8s中污点和容忍度的概念是什么?"
        );
        // messages = List.of("以下请用中文回答问题", "你好~");
        // 构建提示词
        Prompt prompt = new Prompt(messages.stream().map(UserMessage::new).map(Message.class::cast).toList());
        // 流式响应体
        Flux<ChatResponse> flux = model.stream(prompt);
        // 订阅流
        Disposable subscribe = flux
                // .onBackpressureBuffer(1)
                // .limitRate(5)
                .doFirst(() -> {
                    log.info("Start....");
                    ref.start = System.currentTimeMillis();
                })
                .doOnNext(response -> {
                    if (StrUtil.isBlank(ref.id)) {
                        ref.id = response.getMetadata().getId();
                        log.info("id: {}", ref.id);
                    }
                    Generation result = response.getResult();
                    log.info(result.getOutput().getText());
                })
                .doOnComplete(() -> {
                    log.info("Done....");
                    ThreadKit.queue().offer("done");
                })
                .doOnCancel(() -> {
                    ThreadKit.queue().offer("cancel");
                    log.info("Cancel....");
                })
                .doOnError(e -> {
                    log.error("Error....", e);
                    ThreadKit.queue().offer("error");
                })
                .subscribe();

        ThreadKit.run(() -> {
            ThreadKit.sleep(5000);
            // 取消订阅
            subscribe.dispose();
        });

        Object status = ThreadKit.queue().take();
        log.info("status: {}", status);
        log.info("Time-consuming: {}", System.currentTimeMillis() - ref.start);
    }


}
