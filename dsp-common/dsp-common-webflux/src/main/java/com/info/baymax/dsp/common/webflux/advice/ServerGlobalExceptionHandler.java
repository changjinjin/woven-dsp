package com.info.baymax.dsp.common.webflux.advice;

import com.info.baymax.common.message.exception.BizException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * 系统异常统一处理通知
 */
@RestControllerAdvice
@Slf4j
public class ServerGlobalExceptionHandler {

    /**
     * 没有捕获处理的异常
     *
     * @param e 未捕获异常对象
     * @return 未捕获异常消息报文
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @Order(-2)
    public Response<?> uncaughtExceptionHandler(ServerWebExchange swe, Exception e) {
        log.error(e.getMessage(), e);
        ServerHttpResponse response = swe.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage()).build();
    }

    /**
     * 业务处理异常信息
     *
     * @param e 业务异常对象
     * @return 业务异常消息报文
     */
    @ResponseBody
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Order(-3)
    public Response<?> bizExceptionHandler(BizException e) {
        log.error(e.getMessage(), e);
        Response<?> result = Response
            .error(e.getStatus(), StringUtils.defaultIfEmpty(e.getMessage(), "UNKNOWN ERROR:" + e.getStatus()))
            .build();
        if (result != null) {
            return result;
        }
        return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage()).build();
    }

    @ResponseBody
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Order(-4)
    public Response<?> dataAccessException(DataAccessException e) {
        log.error(e.getMessage(), e);
        if (e instanceof DuplicateKeyException) {
            return Response.error(ErrType.INTERNAL_SERVER_ERROR, "Data duplication constraint violation.").details(e.getMessage())
                .build();
        } else {
            return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage()).build();
        }
    }

    @ResponseBody
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Order(-5)
    public Response<?> webExchangeBindException(WebExchangeBindException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String objectName = fieldError.getObjectName();
        StringBuffer buff = new StringBuffer();
        buff.append("[");
        if (StringUtils.isNotEmpty(objectName)) {
            buff.append(objectName).append(".");
        }
        String field = fieldError.getField();
        if (StringUtils.isNotEmpty(objectName)) {
            buff.append(field);
        }
        buff.append("] ").append(fieldError.getDefaultMessage());
        return Response.error(ErrType.BAD_REQUEST, buff.toString()).build();
    }

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Order(-6)
    public Response<?> validationException(ValidationException exception) {
        String message = null;
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) exception;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                message = item.getMessage();
                break;// 拿第一条错误信息即可，满足快速失败就行
            }
        }
        return Response.error(ErrType.BAD_REQUEST, message).build();
    }
}
