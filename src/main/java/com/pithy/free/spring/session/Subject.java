package com.pithy.free.spring.session;

import java.util.Map;

// 会话主体
public class Subject {

    private String sub; // 用户主体
    private String aud; // 接收token主体
    private String iss; // 签发token主体
    private Long iat; // 授权token时间
    private Long exp; // 授权token过期时间
    private Long rxp; // 续期token过期时间
    private Long nbf; // 定义在什么时间之前,该token都是不可用的
    private String jti; // 唯一身份标识,主要用来作为一次性token,从而回避重放攻击
    private Map<String, String> ext;  // 扩展信息

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getRxp() {
        return rxp;
    }

    public void setRxp(Long rxp) {
        this.rxp = rxp;
    }

    public Long getNbf() {
        return nbf;
    }

    public void setNbf(Long nbf) {
        this.nbf = nbf;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public Map<String, String> getExt() {
        return ext;
    }

    public void setExt(Map<String, String> ext) {
        this.ext = ext;
    }
}
