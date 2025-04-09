import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Mr.Lee
 * @since 2025-04-09 18:24
 */

public class MockDataQueryService implements Function<MockDataQueryService.Request, MockDataQueryService.Response> {
    private final Logger log = LoggerFactory.getLogger(MockDataQueryService.class);

    @Override
    public Response apply(Request request) {
        log.info("request.sql: {}", request.sql);
        return new Response(List.of(Map.of("age", 20), Map.of("age", 25)));
    }


    /**
     * 方法的入参
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("给出可执行的SQL查询语句")
    public record Request(
            @JsonProperty(required = true, value = "sql")
            @JsonPropertyDescription("SQL语句")
            String sql) {
    }

    /**
     * 方法的返回值
     */
    public record Response(List<Map<String, Object>> data) { }


}
