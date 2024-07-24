package com.striveonger.common.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Mr.Lee
 * @description: 基于Jackson的JSON生成器
 * @date 2023-09-18 06:23
 */
public class JSONBuilder {
    private final Logger log = LoggerFactory.getLogger(JSONBuilder.class);

    /**
     * TODO: 基于对 ObjectMapper 和 ObjectNode的了解, 重写JsonBuilder
     * https://blog.csdn.net/qq_38688267/article/details/124614982
     */

    private ObjectNode root;

    private String split = "\\.";

    private JSONBuilder() {
        root = JacksonUtils.toObjectNode("{}");
    }

    private JSONBuilder(String split) {
        this.split = split;
        root = JacksonUtils.toObjectNode("{}");
    }

    private JSONBuilder(Map<String, Object> root) {
        this.root = JacksonUtils.toObjectNode(JacksonUtils.toJSONString(root));;
    }

    private JSONBuilder(String split, Map<String, Object> root) {
        this.split = split;
        this.root = JacksonUtils.toObjectNode(JacksonUtils.toJSONString(root));
    }

    public static JSONBuilder builder() {
        return new JSONBuilder();
    }

    public static JSONBuilder builder(String split) {
        return new JSONBuilder(split);
    }

    public static JSONBuilder builder(String split, Map<String, Object> root) {
        return new JSONBuilder(split, root);
    }

    public JSONBuilder reset() {
        root = JacksonUtils.toObjectNode("{}");
        return this;
    }

    public JSONBuilder put(String key, Object value) {
        String[] keys = key.split(split);
        ObjectNode node = root;
        for (int i = 0; i < keys.length - 1; i++) {
            node = node.withObject(keys[i]);
        }
        put(node, keys[keys.length - 1], value);
        return this;
    }

    private void put(ObjectNode node, String key, Object value) {
        if (node == null) {
        } else if(value == null) {
            node.putNull(key);
        } else {
            JsonNode val = JacksonUtils.toJsonNode(value);
            node.set(key, val);
        }
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
}
