package com.pithy.free.spring.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

public class FilterCreator {

    private DispatcherType type;
    private String name;
    private int order;
    private Filter filter;
    private String[] urlPatterns;

    public FilterCreator(DispatcherType type, String name, int order, Filter filter, String... urlPatterns) {
        this.type = type;
        this.name = name;
        this.urlPatterns = urlPatterns;
        this.order = order;
        this.filter = filter;
    }

    public FilterRegistrationBean build() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(type);
        registration.setFilter(filter);
        registration.addUrlPatterns(urlPatterns);
        registration.setName(name);
        registration.setOrder(order);
        return registration;
    }

}