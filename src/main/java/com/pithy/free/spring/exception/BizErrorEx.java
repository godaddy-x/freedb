package com.pithy.free.spring.exception;

public class BizErrorEx extends Exception {

    public static final String BIZ_FAIL = "100000";
    public static final String SYS_FAIL = "900000";

    private String code;
    private String tips;
    private String desc;

    public BizErrorEx(String tips) {
        this(BIZ_FAIL, tips, "", null);
    }

    public BizErrorEx(String tips, Throwable throwable) {
        this(BIZ_FAIL, tips, "", throwable);
    }

    public BizErrorEx(String code, String tips) {
        this(code, tips, "", null);
    }

    public BizErrorEx(String code, String tips, Throwable throwable) {
        this(code, tips, "", throwable);
    }

    public BizErrorEx(String code, String tips, String desc, Throwable throwable) {
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
