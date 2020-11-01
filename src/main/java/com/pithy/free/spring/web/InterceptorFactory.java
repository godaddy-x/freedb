package com.pithy.free.spring.web;

public class InterceptorFactory {

    private InterceptorBean[] interceptorBeans;

    public InterceptorFactory(InterceptorBean... interceptorBeans) {
        this.interceptorBeans = interceptorBeans;
    }

    public InterceptorBean[] getInterceptorBeans() {
        return interceptorBeans;
    }

    public void setInterceptorBeans(InterceptorBean[] interceptorBeans) {
        this.interceptorBeans = interceptorBeans;
    }
}