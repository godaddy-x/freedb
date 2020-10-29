package com.pithy.free.spring.session;

public class AuthConfig {

    private String host; // 授权站点
    private String secret; // 全局密钥
    private Long expire; // 过期时间,默认7天

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
