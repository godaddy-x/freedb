package com.pithy.free.spring.web;

public class RequestObject {

    private String token; // 授权令牌
    private String data; // 请求的JSON数据(base64)
    private Long time; // 请求时间(时间戳/毫秒)
    private String nonce; // 请求随机数
    private String sign; // 签名信息 hmac256(token+data+time+nonce,secret)

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}