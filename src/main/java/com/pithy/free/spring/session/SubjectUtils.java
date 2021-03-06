package com.pithy.free.spring.session;

import com.pithy.free.spring.context.AppConfig;
import com.pithy.free.spring.context.SpringUtils;
import com.pithy.free.spring.exception.AuthErrorEx;
import com.pithy.free.spring.exception.BizErrorEx;
import com.pithy.free.spring.utils.JsonUtil;
import com.pithy.free.spring.web.ParamsFilterWrapper;
import com.pithy.free.spring.web.UnifyRequest;
import com.pithy.free.utils.HMAC256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SubjectUtils {

    private static final Logger log = LoggerFactory.getLogger(SubjectUtils.class);

    private AuthWorker authWorker;

    private List<String> uncheckMethod;

    public SubjectUtils() {

    }

    public SubjectUtils(AuthWorker authWorker) {
        this.authWorker = authWorker;
    }

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

    public static void rebuild404And500Code(HttpServletResponse response) throws BizErrorEx {
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

    public Subject authenticate(HttpServletRequest request, HttpServletResponse response) throws AuthErrorEx {
        if (authWorker == null) {
            log.warn("授权工具[AuthWorker]实例尚未初始化");
            return null;
        }
        String method = request.getRequestURI();
        AppConfig config = SpringUtils.getBean(AppConfig.class);
        String[] useThirdPath = config.getUseThirdPath();
        if (useThirdPath != null && useThirdPath.length > 0) {
            for (int i = 0; i < useThirdPath.length; i++) {
                if (method.equals(useThirdPath[i])) {
                    return null;
                }
            }
        }
        if (!"POST".equals(request.getMethod().toUpperCase()) || !request.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            throw new AuthErrorEx("只允许post方式application/json请求");
        }
        String body = new ParamsFilterWrapper(request).getBodyString(request);
        UnifyRequest reqst = JsonUtil.parseObject(body, UnifyRequest.class);
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
        request.setAttribute(SubjectUtils.CTX_SESSION_VALID, data_byte);
        String[] noAuthPath = config.getNoAuthPath();
        if (noAuthPath == null || noAuthPath.length == 0) {
            return null;
        }
        for (int i = 0; i < noAuthPath.length; i++) {
            if (method.equals(noAuthPath[i])) {
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
        AuthWorker worker = SpringUtils.getBean(AuthWorker.class);
        Subject subject = worker.validToken(token, "");
        if (subject != null) {
            request.setAttribute(SubjectUtils.CTX_SESSION_SUBJECT, subject);
        }
        return subject;
    }

    public List<String> getUncheckMethod() {
        return uncheckMethod;
    }

    public void setUncheckMethod(List<String> uncheckMethod) {
        this.uncheckMethod = uncheckMethod;
    }
}
