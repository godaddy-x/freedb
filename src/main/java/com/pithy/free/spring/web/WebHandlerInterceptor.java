package com.pithy.free.spring.web;

import com.pithy.free.spring.exception.BizErrorEx;
import com.pithy.free.spring.session.SubjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebHandlerInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(WebHandlerInterceptor.class);

    private SubjectUtils subjectUtils;

    public WebHandlerInterceptor() {

    }

    public WebHandlerInterceptor(SubjectUtils subjectUtils) {
        this.subjectUtils = subjectUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SubjectUtils.rebuild404And500Code(response);
        if (subjectUtils == null) {
            throw new BizErrorEx("preHandle SubjectUtils uninitialized");
        }
        subjectUtils.authenticate(request, response);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public SubjectUtils getSubjectUtils() {
        return subjectUtils;
    }

    public void setSubjectUtils(SubjectUtils subjectUtils) {
        this.subjectUtils = subjectUtils;
    }
}
