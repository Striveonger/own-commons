package com.striveonger.common.third.prometheus;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.Jackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mr.Lee
 * @since 2024-08-21 13:55
 */
public class PrometheusHolds {
    private final Logger log = LoggerFactory.getLogger(PrometheusHolds.class);

    /**
     * 配置类
     */
    private final PrometheusConfig config;

    /**
     * 主机地址
     */
    private final String host;

    /**
     * 连接池
     */
    // todo 后面再实现吧...
    // private HttpClient client;
    private PrometheusHolds(PrometheusConfig config) {
        this.config = config;
        this.host = "http://" + config.getHost() + ":" + config.getPort();
    }

    public List<ObjectNode> metadatas(List<Pair<String, String>> filterLabels) {
        StringBuilder url = new StringBuilder(host).append("/api/v1/targets/metadata");
        if (CollUtil.isNotEmpty(filterLabels)) {
            url.append(filterLabels.stream().map(x -> String.format("%s=\"%s\"", x.getKey(), x.getValue())).collect(Collectors.joining(",", "?match_target={", "}")));
        }
        ObjectNode response = Jackson.toObjectNode(get(url.toString()));
        List<ObjectNode> result = new ArrayList<>();
        if (Objects.nonNull(response)) {
            JsonNode status = response.get("status");
            if (Objects.nonNull(status) && "success".equals(status.asText())) {
                JsonNode data = response.get("data");
                if (data instanceof ArrayNode array) {
                    for (JsonNode node : array) {
                        if (node instanceof ObjectNode o) {
                            result.add(o);
                        }
                    }
                }
            }
        }
        return result;
    }

    public List<ObjectNode> targets() {
        String url = host + "/api/v1/targets?state=active";
        String response = get(url);
        List<ObjectNode> targets = new ArrayList<>();
        ArrayNode activeTargets = Optional.ofNullable(Jackson.toObjectNode(response))
                .map(o -> o.get("data")).map(ObjectNode.class::cast)
                .map(o -> o.get("activeTargets")).map(ArrayNode.class::cast)
                .orElse(Jackson.createArrayNode());
        for (JsonNode target : activeTargets) {
            if (target instanceof ObjectNode o) {
                targets.add(o);
            }
        }
        return targets;
    }

    public List<ObjectNode> query(String query) {
        String url = host + "/api/v1/query";
        Map<String, Object> body = new HashMap<>();
        if (StrUtil.isNotBlank(query)) {
            body.put("query", query);
        }
        ObjectNode response = Jackson.toObjectNode(post(url, body));
        List<ObjectNode> result = new ArrayList<>();
        if (Objects.nonNull(response)) {
            JsonNode status = response.get("status");
            if (Objects.nonNull(status) && "success".equals(status.asText())) {
                JsonNode data = response.get("data");
                if (data instanceof ObjectNode objectNode) {
                    if (objectNode.get("result") instanceof ArrayNode arrayNode) {
                        for (JsonNode node : arrayNode) {
                            if (node instanceof ObjectNode o) {
                                result.add(o);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private String get(String url) {
        return HttpUtil.get(url);
    }

    private String post(String url, Map<String, Object> body) {
        return HttpUtil.post(url, body);
    }

    public static class Builder {

        private PrometheusConfig config;

        public static Builder builder() {
            return new Builder();
        }

        public Builder config(PrometheusConfig config) {
            // 校验config完整性
            this.config = config;
            return this;
        }

        public PrometheusHolds build() {
            if (Objects.nonNull(this.config)) {
                return new PrometheusHolds(config);
            }
            throw CustomException.of(ResultStatus.NON_SUPPORT).message("缺少必要的配置项").show();
        }
    }
}
