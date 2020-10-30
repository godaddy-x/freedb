package com.pithy.free.spring.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

// 替换默认的RequestResponseBodyMethodProcessor
@Configuration
@EnableCaching
public class WebMvcConfigurerDevelop implements WebMvcConfigurer, InitializingBean {

    @Autowired(required = false)
    private RequestMappingHandlerAdapter mappingHandlerAdapter;

    @Autowired(required = false)
    private WebHandlerInterceptor handlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(handlerInterceptor).addPathPatterns("/**");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 获取SpringMvc的ReturnValueHandlers
        List<HandlerMethodReturnValueHandler> returnValueHandlers = mappingHandlerAdapter.getReturnValueHandlers();
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
        mappingHandlerAdapter.setReturnValueHandlers(handlers);
    }
}