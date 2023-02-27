package com.here.exception;

import com.here.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * 全局异常处理类
 *
 * @author Lzk
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BizException.class)
    public Response bizExceptionHandler(BizException e) {
        LOGGER.error("发生业务异常，原因：", e);
        return Response.buildFailure("BIZ_ERROR", e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Response methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        LOGGER.error("发生请求参数错误异常，原因：", e);
        return Response.buildFailure("PARAM_ERROR", Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

}
