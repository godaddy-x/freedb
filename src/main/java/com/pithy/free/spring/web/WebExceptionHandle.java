package com.pithy.free.spring.web;

import com.pithy.free.spring.exception.AuthErrorEx;
import com.pithy.free.spring.exception.BizErrorEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebExceptionHandle {

    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandle.class);

    @ExceptionHandler(Exception.class)
    public ResultObject handle(Exception e) {
        if (e instanceof BizErrorEx) {
            BizErrorEx bx = (BizErrorEx) e;
            return new ResultObject(bx.getCode(), bx.getTips(), null, bx.getDesc());
        } else if (e instanceof AuthErrorEx) {
            AuthErrorEx bx = (AuthErrorEx) e;
            return new ResultObject(bx.getCode(), bx.getTips(), null, bx.getDesc());
        } else {
            log.error("系统发生异常", e);
            return new ResultObject(BizErrorEx.SYS_FAIL, "业务处理发生异常,请稍后再尝试");
        }
    }
}