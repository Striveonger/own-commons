package com.striveonger.common.third.kubernetes;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NodeList;
import org.junit.Test;

public class KubernetesHoldsTest {

    @Test
    public void test() throws Exception {
        KubernetesConfig config = new KubernetesConfig();
        KubernetesHolds kubernetes = new KubernetesHolds(config);
        // ApiClient client = kubernetes.defaultClient();
        CoreV1Api api = new CoreV1Api();
        V1NodeList list = api.listNode().execute();
        System.out.println(list);
    }
}