package com.striveonger.common.core;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * JSON 辅助工具
 * @author Mr.Lee
 * @since 2023-02-24 10:42
 */
public class Jackson {

    private static final Logger log = LoggerFactory.getLogger(Jackson.class);
    private static final TypeReference<Map<String, Object>> mapType = new TypeReference<>() {};
    private static final ObjectMapper mapper;

    static {
        mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJSONString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("Jackson Convert String error ", e);
        }
        return null;
    }

    public static Map<String, Object> toMap(String s) {
        try {
            return mapper.readValue(s, mapType);
        } catch (JsonProcessingException e) {
            log.error("Jackson Convert Map error ", e);
        }
        return null;
    }

    public static <T> T toObject(String s, Class<T> clazz) {
        try {
            return mapper.readValue(s, clazz);
        } catch (JsonProcessingException e) {
            log.error("Jackson Convert Object error ", e);
        }
        return null;
    }

    public static <T> TypeReference<T> type() {
        return new TypeReference<>() {};
    }

    public static <T> T toObject(String s, TypeReference<T> type) {
        try {
            return mapper.readValue(s, type);
        } catch (JsonProcessingException e) {
            log.error("Jackson Convert Object error ", e);
        }
        return null;
    }

    public static byte[] toJSONBytes(Object o) {
        String str = toJSONString(o);
        return str == null ? null : str.getBytes(StandardCharsets.UTF_8);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static JsonNode toJsonNode(String props) {
        try {
            return mapper.readTree(props);
        } catch (JsonProcessingException e) {
            // throw new CustomException(ResultStatus.READ_JSON_FAIL);
            log.error("Jackson Convert JsonNode error ", e);
        }
        return null;
    }

    public static JsonNode toJsonNode(Object object) {
        return mapper.valueToTree(object);
    }

    public static ObjectNode toObjectNode(String props) {
        JsonNode node = toJsonNode(props);
        if (node instanceof ObjectNode object) {
            return object;
        }
        log.warn("Jackson Convert ObjectNode error, props is not ObjectNode");
        return null;
    }

    public static ArrayNode toArrayNode(String props) {
        JsonNode node = toJsonNode(props);
        if (node instanceof ArrayNode array) {
            return array;
        }
        log.warn("Jackson Convert ArrayNode error, props is not ArrayNode");
        return null;
    }

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    public static <T> T eval(JsonNode node, String path) {
        return eval(node.toString(), path);
    }

    public static <T> T eval(String json, String path) {
        return JsonPath.read(json, path);
    }

    public static Builder builder() {
        return Builder.builder();
    }

    public static Builder builder(String split) {
        return Builder.builder(split);
    }

    public static Builder builder(Map<String, Object> root) {
        return Builder.builder(null, root);
    }

    public static Builder builder(String split, Map<String, Object> root) {
        return Builder.builder(split, root);
    }

    public static class Builder {
        private ObjectNode root;

        String split = "\\.";

        private Builder() {
            root = Jackson.toObjectNode("{}");
        }

        private Builder(String split) {
            this.split = split;
            root = Jackson.toObjectNode("{}");
        }

        private Builder(Map<String, Object> root) {
            // this.root = JacksonUtils.toObjectNode(JacksonUtils.toJSONString(root));
            this.root = Jackson.getMapper().convertValue(root, ObjectNode.class);
        }

        private Builder(String split, Map<String, Object> root) {
            if (StrUtil.isNotBlank(split)) {
                this.split = split;
            }
            if (Objects.nonNull(root)) {
                this.root = Optional.ofNullable(Jackson.toJSONString(root)).map(Jackson::toObjectNode).orElse(Jackson.toObjectNode("{}"));
            }
        }

        public Builder reset() {
            root = Jackson.toObjectNode("{}");
            return this;
        }

        public Builder put(String key, Object value) {
            String[] keys = key.split(split);
            ObjectNode node = root;
            for (int i = 0; i < keys.length - 1; i++) {
                node = node.withObject(keys[i]);
            }
            put(node, keys[keys.length - 1], value);
            return this;
        }

        public ObjectNode getRootNode() {
            return root;
        }

        @Override
        public String toString() {
            return root.toString();
        }

        public String toJSONString() {
            return toString();
        }

        private void put(ObjectNode node, String key, Object value) {
            if (node == null) {
            } else if(value == null) {
                node.putNull(key);
            } else {
                JsonNode val = Jackson.toJsonNode(value);
                node.set(key, val);
            }
        }

        static Builder builder() {
            return new Builder();
        }

        static Builder builder(String split) {
            return new Builder(split);
        }

        static Builder builder(String split, Map<String, Object> root) {
            return new Builder(split, root);
        }
    }


}
