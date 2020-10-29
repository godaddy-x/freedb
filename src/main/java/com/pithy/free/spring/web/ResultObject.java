package com.pithy.free.spring.web;

import com.pithy.free.utils.IdWorker;

public class ResultObject {

    public static final String SUCC = "000000";

    public ResultObject() {
        this(SUCC, "success", null, "");
    }

    public ResultObject(Object data) {
        this(SUCC, "success", data, "");
    }

    public ResultObject(String code, String tips) {
        this(code, tips, null, "");
    }

    public ResultObject(String code, String tips, Object data, String desc) {
        this.code = code;
        this.tips = tips;
        this.data = data;
        this.desc = desc;
        this.time = System.currentTimeMillis();
        this.nonce = IdWorker.getSID();
    }

    private String code; // 业务代码 000000.成功
    private String tips; // 业务信息
    private Object data; // 响应数据
    private String desc; // 业务代码描述
    private Long time; // 响应时间
    private String nonce; // 响应随机数

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}