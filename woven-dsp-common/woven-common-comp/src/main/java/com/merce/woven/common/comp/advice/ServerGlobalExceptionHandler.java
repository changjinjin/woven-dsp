package com.merce.woven.common.comp.advice;

import com.merce.woven.common.message.exception.BizException;
import com.merce.woven.common.message.result.ErrType;
import com.merce.woven.common.message.result.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
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
     * 认证授权异常信息
     *
     * @param e 认证授权异常对象
     * @return 认证授权异常消息报文
     */
    /*
     * @ResponseBody
     *
     * @ExceptionHandler(AuthenticationException.class)
     *
     * @Order(-4) public Response<?> authenticationExceptionHandler(ServerWebExchange swe, AuthenticationException e) {
     * logger.error(e.getMessage(), e);
     *
     * ServerHttpResponse response = swe.getResponse(); Response<Object> resp =
     * AuthenticationExceptionHandler.handle(response, e); if (resp != null) { return resp; }
     *
     * HttpStatus httpStatus = defaultHttpStatus(response, HttpStatus.UNAUTHORIZED); Response<?> result =
     * Response.error(httpStatus.value(), StringUtils.defaultIfEmpty(e.getMessage(), "UNKNOWN ERROR:" +
     * httpStatus.value())); if (result != null) return result;
     *
     * // 如果没有处理成功，这抛出统一的认证失败消息 return Response.error(ErrType.UNAUTHORIZED); }
     */

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
