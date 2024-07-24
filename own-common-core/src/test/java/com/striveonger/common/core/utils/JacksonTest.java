package com.striveonger.common.core.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-14 02:35
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
        JSONBuilder builder = JSONBuilder.builder("\\.");
        builder.put("a.b.c.d", "test").put("a.b.c.e", null);
        builder.put("a.x.y.z", map);

        System.out.println(builder.getRootNode());

    }

}
