package com.pithy.free.spring.web;

import org.springframework.web.servlet.HandlerInterceptor;

public class InterceptorBean {

    private HandlerInterceptor interceptor; // 拦截器
    private String[] addPathPatterns; // 用于添加拦截规则
    private String[] excludePathPatterns; // 用户排除拦截

    public InterceptorBean(HandlerInterceptor interceptor, String... addPathPatterns) {
        this(interceptor, addPathPatterns, new String[]{});
    }

    public InterceptorBean(HandlerInterceptor interceptor, String[] addPathPatterns, String[] excludePathPatterns) {
        this.interceptor = interceptor;
        this.addPathPatterns = addPathPatterns;
        this.excludePathPatterns = excludePathPatterns;
        if (this.excludePathPatterns == null) {
            this.excludePathPatterns = new String[]{};
        }
    }

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public String[] getAddPathPatterns() {
        return addPathPatterns;
    }

    public void setAddPathPatterns(String[] addPathPatterns) {
        this.addPathPatterns = addPathPatterns;
    }

    public String[] getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(String[] excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }
}