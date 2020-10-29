package com.pithy.free.spring.context;

import org.springframework.boot.context.properties.ConfigurationProperties;

public class AppConfig {

    private String globalSecret; // 全局密钥
    private String modelPath; // 数据模型路径
    private String[] noAuthPath; // 无需校验登录路径

    public String getGlobalSecret() {
        return globalSecret;
    }

    public void setGlobalSecret(String globalSecret) {
        this.globalSecret = globalSecret;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String[] getNoAuthPath() {
        return noAuthPath;
    }

    public void setNoAuthPath(String[] noAuthPath) {
        this.noAuthPath = noAuthPath;
    }

}
