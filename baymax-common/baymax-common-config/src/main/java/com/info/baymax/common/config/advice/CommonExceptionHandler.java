package com.info.baymax.common.config.advice;

import com.info.baymax.common.core.exception.BizException;
import com.info.baymax.common.core.result.ErrMsg;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * 系统异常统一处理通知
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @Autowired
    @Nullable
    private MessageSourceAccessor accessor;

    /**
     * 没有捕获处理的异常
     *
     * @param e 未捕获异常对象
     * @return 未捕获异常消息报文
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @Order(1)
    public Response<?> uncaughtExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return Response.error(ErrType.SERVICE_UNAVAILABLE, e.getMessage()).details(e).build();
    }

    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class, ServerWebInputException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Order(0)
    public Response<?> illegalArgumentExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return Response.error(ErrType.BAD_REQUEST, e.getMessage()).details(e).build();
    }

    /**
     * 业务处理异常信息
     *
     * @param e 业务异常对象
     * @return 业务异常消息报文
     */
    @ResponseBody
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @Order(-1)
    public Response<?> bizExceptionHandler(BizException e) {
        log.error(e.getMessage(), e);
        Response<?> result = null;

        Throwable cause = e.getCause();
        ErrMsg errMsg = e.getErrMsg();
        String customMessage = e.getCustomMessage();

        String message = null;
        if (accessor != null) {
            message = accessor.getMessage(errMsg.getCode(), e.getArgs(), e.getCustomMessage());
        }

        message = StringUtils.defaultIfEmpty(message, customMessage);
        if (cause != null) {
            if (cause instanceof BizException) {
                BizException c = (BizException) cause;
                result = Response
                    .error(errMsg.getStatus(),
                        StringUtils.defaultIfEmpty(message, "UNKNOWN ERROR:" + errMsg.getStatus()))
                    .details(c.toString()).build();
            } else {
                result = Response.error(errMsg.getStatus(), cause.getMessage()).details(cause.toString()).build();
            }
        } else {
            result = Response
                .error(errMsg.getStatus(),
                    StringUtils.defaultIfEmpty(message, "UNKNOWN ERROR:" + errMsg.getStatus()))
                .details(e.toString()).build();
        }
        if (result != null) {
            return result;
        }
        return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage()).details(e).build();
    }

    @ResponseBody
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @Order(-2)
    public Response<?> dataAccessException(DataAccessException e) {
        log.error(e.getMessage(), e);
        String message = null;
        Throwable cause = e.getCause();
        if (accessor != null) {
            if (cause != null) {
                message = accessor.getMessage(cause.getClass().getCanonicalName());
            } else {
                message = accessor.getMessage(e.getClass().getCanonicalName());
            }
        }
        message = StringUtils.defaultIfEmpty(message, e.getMessage());
        return Response.serviceUnavailable().message(message).details(e).build();
    }

    @ResponseBody
    @ExceptionHandler(value = {BindException.class, WebExchangeBindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Order(-3)
    public Response<?> webExchangeBindException(BindingResult e) {
        FieldError fieldError = e.getFieldError();
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
        return Response.badRequest().message(buff.toString()).details(e).build();
    }

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Order(-4)
    public Response<?> validationException(ValidationException e) {
        String message = null;
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                message = item.getMessage();
                break;// 拿第一条错误信息即可，满足快速失败就行
            }
        } else {
            message = e.getMessage();
        }
        return Response.badRequest().message(message).details(e).build();
    }
}
