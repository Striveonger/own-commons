package com.striveonger.common.prometheus;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.striveonger.common.core.constant.ResultStatus;
import com.striveonger.common.core.exception.CustomException;
import com.striveonger.common.core.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Mr.Lee
 * @since 2024-08-21 13:55
 */
public class PrometheusSource {
    private final Logger log = LoggerFactory.getLogger(PrometheusSource.class);

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
    private PrometheusSource(PrometheusConfig config) {
        this.config = config;
        this.host = "http://" + config.getHost() + ":" + config.getPort();
    }

    public List<ObjectNode> getMetaDataList(List<Pair<String, String>> labels) {
        StringBuilder url = new StringBuilder(host).append("/api/v1/targets/metadata");
        if (CollUtil.isNotEmpty(labels)) {
            url.append(labels.stream().map(x -> String.format("%s=\"%s\"", x.getKey(), x.getValue())).collect(Collectors.joining(",", "?match_target={", "}")));
        }
        ObjectNode response = JacksonUtils.toObjectNode(get(url.toString()));
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


    private String get(String url) {
        return HttpUtil.get(url);
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

        public PrometheusSource build() {
            if (Objects.nonNull(this.config)) {
                return new PrometheusSource(config);
            }
            throw CustomException.create(ResultStatus.NON_SUPPORT).message("缺少必要的配置项").show();
        }
    }
}
