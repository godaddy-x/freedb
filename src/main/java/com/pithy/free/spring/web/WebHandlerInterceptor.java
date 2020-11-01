package com.pithy.free.spring.web;

import com.pithy.free.spring.exception.BizErrorEx;
import com.pithy.free.spring.session.SubjectHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebHandlerInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(WebHandlerInterceptor.class);

    private SubjectHolder subjectHolder;

    public WebHandlerInterceptor() {

    }

    public WebHandlerInterceptor(SubjectHolder subjectHolder) {
        this.subjectHolder = subjectHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SubjectHolder.rebuild404And500Code(response);
        if (subjectHolder == null) {
            throw new BizErrorEx("preHandle subjectHolder uninitialized");
        }
        subjectHolder.authenticate(request, response);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public SubjectHolder getSubjectHolder() {
        return subjectHolder;
    }

    public void setSubjectHolder(SubjectHolder subjectHolder) {
        this.subjectHolder = subjectHolder;
    }
}
