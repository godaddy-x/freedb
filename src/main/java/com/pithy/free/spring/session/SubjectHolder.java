package com.pithy.free.spring.session;

import com.pithy.free.spring.exception.BizErrorEx;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SubjectHolder {

    public static final String CTX_SESSION_SUBJECT = "ctx_session_subject";
    public static final String CTX_SESSION_VALID = "ctx_session_valid";

    public static Subject get() throws BizErrorEx {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new BizErrorEx("当前线程中不存在Request上下文");
        }
        HttpServletRequest request = attrs.getRequest();
        Object object = request.getAttribute(CTX_SESSION_SUBJECT);
        if (object == null) {
            throw new BizErrorEx("无法找到用户主体");
        }
        if (object instanceof Subject) {
            return (Subject) object;
        }
        return null;
    }

}
