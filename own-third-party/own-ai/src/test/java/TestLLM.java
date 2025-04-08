import com.agentsflex.core.llm.Llm;
import com.agentsflex.core.llm.client.OkHttpClientUtil;
import com.agentsflex.core.llm.response.AiMessageResponse;
import com.agentsflex.core.message.AiMessage;
import com.agentsflex.core.message.HumanMessage;
import com.agentsflex.core.message.MessageStatus;
import com.agentsflex.core.prompt.FunctionPrompt;
import com.agentsflex.core.prompt.HistoriesPrompt;
import com.agentsflex.core.util.StringUtil;
import com.agentsflex.llm.ollama.OllamaLlm;
import com.agentsflex.llm.ollama.OllamaLlmConfig;
import com.agentsflex.llm.siliconflow.SiliconflowConfig;
import com.agentsflex.llm.siliconflow.SiliconflowLlm;
import com.striveonger.common.ai.prompt.CallPrompt;
import com.striveonger.common.core.Jackson;
import com.striveonger.common.core.Timepiece;
import com.striveonger.common.core.thread.ThreadKit;
import okhttp3.OkHttpClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Lee
 * @since 2025-03-11 23:11
 */
public class TestLLM {
    private final Logger log = LoggerFactory.getLogger(TestLLM.class);

    @Test
    public void localOllamaChat() {
        Timepiece timepiece = Timepiece.of("llm");

        OllamaLlmConfig config = new OllamaLlmConfig();
        config.setEndpoint("http://10.13.147.1:11434");
        // config.setModel("deepseek-r1:14b");
        // config.setModel("llama3.1:8b");
        config.setModel("qwq");
        config.setDebug(true);
        timepiece.mark("llm config");

        Llm llm = new OllamaLlm(config);
        llm.chatStream("你好~", (context, response) -> {
            AiMessage message = response.getMessage();
            System.out.println(message.getContent());

            if (message.getStatus() == MessageStatus.END) {
                ThreadKit.queue().offer(message.getFullContent());
            }
        });

        // AiMessageResponse response = llm.chat(new TextPrompt("你好~"));
        // System.out.println(response.getMessage().getContent());

        Object o = ThreadKit.queue().take();
        log.info("result = {}", o);

        timepiece.mark("llm chat");
        timepiece.show();

        log.info("done.....");
    }

    @Test
    public void siliconflowChat() {
        // 开启计时器
        Timepiece timepiece = Timepiece.of("llm");
        // 配置硅基流动 ApiKey & 所用模型
        SiliconflowConfig config = new SiliconflowConfig();
        config.setApiKey("sk-senryjndosezrwzpowellxiinfsoiyzpmazedoqicqllvlje");
        config.setModel("deepseek-ai/DeepSeek-R1-Distill-Qwen-7B");

        timepiece.mark("llm config");

        Llm llm = new SiliconflowLlm(config);
        llm.chatStream("你好~", (context, response) -> {
            AiMessage message = response.getMessage();
            System.out.println(message.getContent());
            if (message.getStatus() == MessageStatus.END) {
                ThreadKit.queue().offer(message.getFullContent());
            }
        });

        Object o = ThreadKit.queue().take();
        log.info("result = {}", o);

        timepiece.mark("llm chat");
    }

    @Test
    public void localOllamaCallback() throws Exception {

        InputStream in = new FileInputStream("/Users/striveonger/development/workspace/favor/own-commons/own-third-party/own-ai/src/test/resources/changelog.json");
        String changelog = Jackson.builder().read(in).build().toString();

        // 开启计时器
        Timepiece timepiece = Timepiece.of("llm");

        OkHttpClient.Builder builder = (new OkHttpClient.Builder()).connectTimeout(30L, TimeUnit.MINUTES).readTimeout(30L, TimeUnit.MINUTES);
        String proxyHost = System.getProperty("https.proxyHost");
        String proxyPort = System.getProperty("https.proxyPort");
        if (StringUtil.hasText(proxyHost) && StringUtil.hasText(proxyPort)) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(proxyHost.trim(), Integer.parseInt(proxyPort.trim()));
            builder.proxy(new Proxy(Proxy.Type.HTTP, inetSocketAddress));
        }
        OkHttpClientUtil.setOkHttpClientBuilder(builder);

        OllamaLlmConfig config = new OllamaLlmConfig();
        config.setEndpoint("http://10.13.147.1:11434");
        // config.setModel("deepseek-r1:14b");
        config.setModel("llama3.1:8b");
        // config.setModel("qwq");
        config.setDebug(true);
        timepiece.mark("llm config");

        Llm llm = new OllamaLlm(config);
        CallPrompt prompt = new CallPrompt("我现在想知道每天晚上10点之后, 还在购物的用户年龄", GenerateSQLFunctions.class);
        prompt.setHistoryMessages(
                List.of(
                        new HumanMessage("你现在的角色是DBA"),
                        new HumanMessage("现维护的MySQL库中有四张数据表, 以下是用Liquibase的JSON格式描述的表结构 \n" + changelog)
                )
        );
        AiMessageResponse response = llm.chat(prompt);
        if (response.isError()) {
            System.out.println(response.getErrorMessage());
        }
        System.out.println(response.callFunctions());
        timepiece.show();

        timepiece.mark("llm chat");
    }

    @Test
    public void siliconflowCallback() throws Exception {

        InputStream in = new FileInputStream("/Users/striveonger/development/workspace/favor/own-commons/own-third-party/own-ai/src/test/resources/changelog.json");
        String changelog = Jackson.builder().read(in).build().toString();

        // 开启计时器
        Timepiece timepiece = Timepiece.of("llm");

        // 配置硅基流动 ApiKey & 所用模型
        SiliconflowConfig config = new SiliconflowConfig();
        config.setApiKey("sk-senryjndosezrwzpowellxiinfsoiyzpmazedoqicqllvlje");
        config.setModel("deepseek-ai/DeepSeek-R1-Distill-Qwen-7B");

        timepiece.mark("llm config");

        Llm llm = new SiliconflowLlm(config);
        CallPrompt prompt = new CallPrompt("我现在想知道每天晚上10点之后, 还在购物的用户年龄", GenerateSQLFunctions.class);
        prompt.setHistoryMessages(
                List.of(
                        new HumanMessage("你现在的角色是DBA"),
                        new HumanMessage("现维护的MySQL库中有四张数据表, 以下是用Liquibase的JSON格式描述的表结构 \n" + changelog)
                )
        );
        AiMessageResponse response = llm.chat(prompt);
        if (response.isError()) {
            System.out.println(response.getErrorMessage());
        }
        System.out.println(response.callFunctions());
        timepiece.show();

        timepiece.mark("llm chat");
    }

}
