package com.striveonger.common.core.utils;

import cn.hutool.core.lang.Dict;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Mr.Lee
 * @description:
 * @date 2024-07-14 02:35
 */
public class JacksonTest {
    private final Logger log = LoggerFactory.getLogger(JacksonTest.class);

    @Test
    public void test() throws Exception {
        String s = JacksonUtils.toJSONString(List.of(Dict.of("a", 1, "b", 2, "c", LocalDateTime.now())));

        // System.out.println(s);

        // ObjectNode node = JacksonUtils.readObjectNode(s);
        // assert node != null;
        // node.put("test", "test");
        // System.out.println(JacksonUtils.toJSONString(node));

        JacksonUtils.readArrayNode(s).forEach(System.out::println);

    }

}
