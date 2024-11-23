package com.striveonger.common.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.striveonger.common.core.result.Result;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Mr.Lee
 * @since 2024-07-14 02:35
 */
public class JacksonTest {
    private final Logger log = LoggerFactory.getLogger(JacksonTest.class);

    @Test
    public void test() throws Exception {
        // String s = JacksonUtils.toJSONString(List.of(Dict.of("a", 1, "b", 2, "c", LocalDateTime.now())));

        // System.out.println(s);

        // ObjectNode node = JacksonUtils.readObjectNode(s);
        // assert node != null;
        // node.put("test", "test");
        // System.out.println(JacksonUtils.toJSONString(node));

        // JacksonUtils.readArrayNode(s).forEach(System.out::println);

        Map<String, Object> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", "2");
        map.put("C", LocalDateTime.now());
        map.put("D", List.of(1, 2, 3, 4, 5));
        // JSONBuilder builder = JSONBuilder.builder("\\.", map);
        // System.out.println(builder.getRootNode());
        // builder.reset();
        Jackson.Builder builder = Jackson.builder();
        builder.put("a.b.c.d", "test").put("a.b.c.e", null);
        builder.put("a.x.y.z", map);

        System.out.println(builder.getRootNode());

    }

    @Test
    public void test2() {
        // https://github.com/json-path/JsonPath 使用文档

        ObjectNode root = Jackson.builder()
                .put("A.B.C", "sss")
                .put("A.B.D", 2)
                .put("A.B.E", 3.14)
                .put("A.B.F", List.of("a", "b", "c")).getRootNode();
        String x = Jackson.eval(root, "$.A.B.C");
        System.out.println(x);
        List<String> eval = Jackson.eval(root, "$.A.B.F[1:]");
        System.out.println(eval);

        int d = Jackson.eval(root, "$.A.B.D");
        System.out.println(d);
        double f = Jackson.eval(root, "$.A.B.E");
        System.out.println(f);

        ArrayNode array = Jackson.createArrayNode();
        array.add(root);
        array.add(root);
        System.out.println(array);

        List<Double> fs = Jackson.eval(array, "$..A.B.E");
        System.out.println(fs);

    }

    @Test
    public void test3() {
        String s = Jackson.toJSONString(Result.success().message("so so so easy"));

        Result result = Optional.ofNullable(Jackson.toObject(s, Result.class)).orElse(Result.accident());
        System.out.println(result.getMessage());

        Object xxx = "SSS";
        JsonNode node = Jackson.toJsonNode(xxx);
        System.out.println(node);
    }

}
