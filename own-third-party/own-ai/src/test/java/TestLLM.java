import com.agentsflex.core.llm.Llm;
import com.agentsflex.core.llm.response.AiMessageResponse;
import com.agentsflex.core.prompt.TextPrompt;
import com.agentsflex.llm.ollama.OllamaLlm;
import com.agentsflex.llm.ollama.OllamaLlmConfig;
import com.striveonger.common.core.Timepiece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @since 2025-03-11 23:11
 */
public class TestLLM {
    private final Logger log = LoggerFactory.getLogger(TestLLM.class);

    public static void main(String[] args) {
        Timepiece timepiece = Timepiece.of("llm");

        OllamaLlmConfig config = new OllamaLlmConfig();
        config.setEndpoint("http://10.13.147.1:11434");
        config.setModel("deepseek-r1:14b");
        // config.setModel("llama3.1:latest");
        config.setDebug(true);
        timepiece.mark("llm config");

        Llm llm = new OllamaLlm(config);
        llm.chatStream("你好~", (context, response) -> {
            System.out.println(response.getMessage().getContent());
        });

        // AiMessageResponse response = llm.chat(new TextPrompt("你好~"));
        // System.out.println(response.getMessage().getContent());

        timepiece.mark("llm chat");
        timepiece.show();

        System.out.println("done...");
    }

}
