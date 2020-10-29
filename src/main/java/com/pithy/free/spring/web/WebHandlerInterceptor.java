package com.pithy.free.spring.web;

import com.pithy.free.spring.context.AppConfig;
import com.pithy.free.spring.context.SpringUtils;
import com.pithy.free.spring.exception.AuthErrorEx;
import com.pithy.free.spring.exception.BizErrorEx;
import com.pithy.free.spring.session.AuthWorker;
import com.pithy.free.spring.session.Subject;
import com.pithy.free.spring.session.SubjectHolder;
import com.pithy.free.utils.HMAC256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class WebHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthWorker authWorker;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        doSafeAuthToken(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // System.out.println("postHandle");
        checkResponseCode(response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // System.out.println("afterCompletion");
    }

    private void checkResponseCode(HttpServletResponse response) throws BizErrorEx {
        int status = response.getStatus();
        if (status == 404) {
            response.setStatus(200);
            throw new BizErrorEx("无效的请求方法");
        }
        if (status == 500) {
            response.setStatus(200);
            throw new BizErrorEx("业务方法处理异常");
        }
    }

    private Subject doSafeAuthToken(HttpServletRequest request) throws AuthErrorEx {
        if (!"POST".equals(request.getMethod().toUpperCase()) || !request.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            throw new AuthErrorEx("只允许post方式application/json请求");
        }
        String body = new ParameterRequestWrapper(request).getBodyString(request);
        RequestObject reqst = JsonUtil.parseObject(body, RequestObject.class);
        if (reqst == null) {
            throw new AuthErrorEx("请求数据不能为空");
        }
        if (StringUtils.isEmpty(reqst.getData())) {
            throw new AuthErrorEx("业务数据不能为空");
        }
        byte[] data_byte = Base64Utils.decodeFromString(reqst.getData());
        if (data_byte == null || data_byte.length == 0) {
            throw new AuthErrorEx("业务数据解析失败");
        }
        request.setAttribute(SubjectHolder.CTX_SESSION_VALID, data_byte);
        AppConfig config = SpringUtils.getBean(AppConfig.class);
        AuthWorker worker = SpringUtils.getBean(AuthWorker.class);
        String[] path = config.getNoAuthPath();
        if (path == null || path.length == 0) {
            return null;
        }
        String method = request.getRequestURI();
        for (int i = 0; i < path.length; i++) {
            if (method.equals(path[i])) {
                return null;
            }
        }
        String token = reqst.getToken();
        String data = reqst.getData();
        Long time = reqst.getTime();
        String nonce = reqst.getNonce();
        String sign = reqst.getSign();
        if (StringUtils.isEmpty(token)) {
            throw new AuthErrorEx("授权令牌不能为空");
        }
        if (StringUtils.isEmpty(data)) {
            throw new AuthErrorEx("请求数据不能为空");
        }
        if (StringUtils.isEmpty(nonce)) {
            throw new AuthErrorEx("请求随机数不能为空");
        }
        if (StringUtils.isEmpty(sign)) {
            throw new AuthErrorEx("数据签名不能为空");
        }
        if (time == null) {
            throw new AuthErrorEx("请求时间不能为空");
        }
        if (Math.abs(System.currentTimeMillis() - time) > 600000) {
            throw new AuthErrorEx("数据签名已失效");
        }
        String secret = authWorker.createSecret(token);
        String checkSign = HMAC256.create(new StringBuffer(token).append(data).append(time).append(nonce).toString(), secret);
        if (!sign.equals(checkSign)) {
            throw new AuthErrorEx("数据签名校验失败");
        }
        Subject subject = worker.validToken(token, "");
        if (subject != null) {
            request.setAttribute(SubjectHolder.CTX_SESSION_SUBJECT, subject);
        }
        return subject;
    }

}
