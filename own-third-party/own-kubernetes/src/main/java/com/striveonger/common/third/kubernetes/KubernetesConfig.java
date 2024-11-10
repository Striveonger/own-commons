package com.striveonger.common.third.kubernetes;

/**
 * @author Mr.Lee
 * @since 2024-11-10 11:11
 */
public class KubernetesConfig {

    private boolean enabled;
    /**
     * k8s 配置文件路径
     */
    private String kubeConfigPath;

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getKubeConfigPath() {
        return kubeConfigPath;
    }

    public void setKubeConfigPath(String kubeConfigPath) {
        this.kubeConfigPath = kubeConfigPath;
    }
}
