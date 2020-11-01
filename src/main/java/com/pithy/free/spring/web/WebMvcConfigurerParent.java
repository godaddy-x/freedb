package com.pithy.free.spring.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

// 替换默认的RequestResponseBodyMethodProcessor
public class WebMvcConfigurerParent implements WebMvcConfigurer, InitializingBean {

    @Autowired(required = false)
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired(required = false)
    private InterceptorFactory interceptorFactory;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 增加自定义拦截器列表
        if (interceptorFactory == null) {
            return;
        }
        InterceptorBean[] beans = interceptorFactory.getInterceptorBeans();
        if (beans == null || beans.length == 0) {
            return;
        }
        for (InterceptorBean bean : beans) {
            registry.addInterceptor(bean.getInterceptor()).addPathPatterns(bean.getAddPathPatterns()).excludePathPatterns(bean.getExcludePathPatterns());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 获取SpringMvc的ReturnValueHandlers
        List<HandlerMethodReturnValueHandler> returnValueHandlers = handlerAdapter.getReturnValueHandlers();
        // 新建一个List来保存替换后的Handler的List
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(returnValueHandlers);
        // 循环遍历找出RequestResponseBodyMethodProcessor
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                // 创建定制的Json格式处理Handler
                UnifyReturnValueHandler decorator = new UnifyReturnValueHandler(handler);
                // 使用定制的Json格式处理Handler替换原有的RequestResponseBodyMethodProcessor
                handlers.set(handlers.indexOf(handler), decorator);
                break;
            }
        }
        // 重新设置SpringMVC的ReturnValueHandlers
        handlerAdapter.setReturnValueHandlers(handlers);
    }

    public RequestMappingHandlerAdapter getHandlerAdapter() {
        return handlerAdapter;
    }

    public void setHandlerAdapter(RequestMappingHandlerAdapter handlerAdapter) {
        this.handlerAdapter = handlerAdapter;
    }

    public InterceptorFactory getInterceptorFactory() {
        return interceptorFactory;
    }

    public void setInterceptorFactory(InterceptorFactory interceptorFactory) {
        this.interceptorFactory = interceptorFactory;
    }
}