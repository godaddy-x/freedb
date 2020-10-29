package com.pithy.free.spring.exception;

import java.io.IOException;

public class AuthErrorEx extends IOException {

    public static final String AUTH_FAIL = "200000";
    public static final String AUTH_OUT_FAIL = "200001";

    private String code;
    private String tips;
    private String desc;

    public AuthErrorEx(String tips) {
        this(AUTH_FAIL, tips, "", null);
    }

    public AuthErrorEx(Throwable throwable) {
        this(AUTH_FAIL, "fail", "", throwable);
    }

    public AuthErrorEx(String tips, Throwable throwable) {
        this(AUTH_FAIL, tips, "", throwable);
    }

    public AuthErrorEx(String code, String tips) {
        this(code, tips, "", null);
    }

    public AuthErrorEx(String code, String tips, Throwable throwable) {
        this(code, tips, "", throwable);
    }

    public AuthErrorEx(String code, String tips, String desc, Throwable throwable) {
        super(tips, throwable);
        this.code = code;
        this.tips = tips;
        this.desc = desc;
    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
