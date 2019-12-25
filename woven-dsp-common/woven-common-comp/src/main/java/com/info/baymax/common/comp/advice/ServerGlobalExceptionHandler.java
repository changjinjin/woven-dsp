package com.info.baymax.common.comp.advice;

import com.info.baymax.common.message.exception.BizException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

/**
 * 系统异常统一处理通知
 *
 * @author yjw@jusfoun.com
 * @date 2017年12月18日 下午6:12:38
 */
@RestControllerAdvice
public class ServerGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerGlobalExceptionHandler.class);

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
        logger.error(e.getMessage(), e);
        ServerHttpResponse response = swe.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 业务处理异常信息
     *
     * @param e 业务异常对象
     * @return 业务异常消息报文
     */
    @ResponseBody
    @ExceptionHandler(BizException.class)
    @Order(-3)
    public Response<?> bizExceptionHandler(ServerWebExchange swe, BizException e) {
        logger.error(e.getMessage(), e);

        ServerHttpResponse response = swe.getResponse();
        Response<?> result = Response.error(e.getStatus(),
            StringUtils.defaultIfEmpty(e.getMessage(), "UNKNOWN ERROR:" + e.getStatus()));
        if (result != null) {
            response.setStatusCode(HttpStatus.OK);
            return result;
        }
        response.setStatusCode(HttpStatus.OK);
        return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 数据库异常
     *
     * @param e 业务异常对象
     * @return 业务异常消息报文
     */

    @ResponseBody
    @ExceptionHandler(DataAccessException.class)
    @Order(-4)
    public Response<?> DataAccessException(ServerWebExchange swe, DataAccessException e) {
        logger.error(e.getMessage(), e);
        ServerHttpResponse response = swe.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        if (e instanceof DuplicateKeyException) {
            return Response.error(ErrType.INTERNAL_SERVER_ERROR, "数据重复");
        } else {
            return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 设置Response默认的状态码
     *
     * @param response          处理前的response
     * @param defaultHttpStatus 如果当前默认的
     * @return 设置的默认的状态码
     */
    private HttpStatus defaultHttpStatus(ServerHttpResponse response, HttpStatus defaultHttpStatus) {
        HttpStatus httpStatus = response.getStatusCode();
        if (httpStatus == null)
            httpStatus = defaultHttpStatus;
        response.setStatusCode(httpStatus);
        return httpStatus;
    }
}
