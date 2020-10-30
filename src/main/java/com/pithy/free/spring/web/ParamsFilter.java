package com.pithy.free.spring.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ParamsFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        ServletRequest wrapper = null;
        if (request instanceof HttpServletRequest) {
            wrapper = new ParamsFilterWrapper((HttpServletRequest) request);
        }
        if (wrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(wrapper, response);
        }
    }

    @Override
    public void init(FilterConfig arg) {
    }


}