package com.striveonger.common.third.kubernetes;

import cn.hutool.core.util.StrUtil;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mr.Lee
 * @since 2024-08-26 09:54
 */
public class KubernetesHolds {
    private final Logger log = LoggerFactory.getLogger(KubernetesHolds.class);

    /**
     * 默认的创建方式
     */
    public KubernetesHolds(KubernetesConfig config) {
        try {
            ApiClient client;
            if (Objects.isNull(config) || StrUtil.isBlank(config.getKubeConfigPath())) {
                // 默认设置: `~/.kube/config`
                client = Config.defaultClient();
            } else {
                // 自定义设置
                client = Config.fromConfig(config.getKubeConfigPath());
            }
            Configuration.setDefaultApiClient(client);
        } catch (IOException e) {
            log.error("k8s config error", e);
        }
    }

    public ApiClient defaultClient() {
        return Configuration.getDefaultApiClient();
    }

    public List<V1Deployment> getAllDeploymentList() {
        AppsV1Api api = new AppsV1Api();
        try {
            V1DeploymentList list = api.listDeploymentForAllNamespaces().execute();
            return list.getItems();
        } catch (ApiException e) {
            log.error("kubernetes list all deployments error", e);
        }
        return List.of();
    }

    public List<V1Pod> getAllPodList() {
        CoreV1Api api = new CoreV1Api();
        try {
            V1PodList list = api.listPodForAllNamespaces().execute();
            return list.getItems();
        } catch (ApiException e) {
            log.error("kubernetes list all pods error", e);
        }
        return List.of();
    }

    public List<V1Pod> getPodListByNamespace(String namespace) {
        CoreV1Api api = new CoreV1Api();
        try {
            V1PodList list = api.listNamespacedPod(namespace).execute();
            return list.getItems();
        } catch (ApiException e) {
            log.error("kubernetes list all pods error", e);
        }
        return List.of();
    }

    public V1Pod getPod(String pod, String namespace) {
        CoreV1Api api = new CoreV1Api();
        try {
            return api.readNamespacedPod(pod, namespace).execute();
        } catch (ApiException e) {
            log.error("kubernetes get pod error, podName: {}, namespace: {}", pod, namespace, e);
        }
        return null;
    }

    public List<V1Node> getNodeList() {
        CoreV1Api api = new CoreV1Api();
        try {
            V1NodeList nodes = api.listNode().execute();
            return new ArrayList<>(nodes.getItems());
        } catch (ApiException e) {
            log.error("kubernetes list all nodes error", e);
        }
        return List.of();
    }
}
