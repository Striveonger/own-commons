package com.striveonger.common.third.kubernetes;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author Mr.Lee
 * @since 2024-08-26 09:54
 */
public class KubernetesHolds {
    private final Logger log = LoggerFactory.getLogger(KubernetesHolds.class);

    /**
     * 默认的创建方式
     * todo: 后面如有需要, 可扩展其他创建方式
     */
    public KubernetesHolds() {
        try {
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
        } catch (IOException e) {
            log.error("k8s config error", e);
            // throw new CustomIllegalStateException("未能初始化k8s客户端", SERVICE_NOT_AVAILABLE);
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
}
