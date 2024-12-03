package com.striveonger.common.third.kubernetes;

import cn.hutool.core.collection.CollUtil;
import io.kubernetes.client.openapi.models.V1Deployment;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class KubernetesKitTest {

    @Test
    public void test() {
        KubernetesKit k8s = new KubernetesKit(null);
        List<V1Deployment> list = k8s.getAllDeploymentList();
        Assert.assertTrue(CollUtil.isNotEmpty(list));
        System.out.println(list);
    }
}