package com.info.baymax.dsp.common.webmvc.advice;

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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统异常统一处理通知
 */
@RestControllerAdvice
public class ServletGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServletGlobalExceptionHandler.class);

    /**
     * 没有捕获处理的异常
     *
     * @param e 未捕获异常对象
     * @return 未捕获异常消息报文
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @Order(-2)
    public Response<?> uncaughtExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error(e.getMessage(), e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
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
    public Response<?> bizExceptionHandler(HttpServletRequest request, HttpServletResponse response, BizException e) {
        logger.error(e.getMessage(), e);

        Response<?> result = Response.error(e.getStatus(),
            StringUtils.defaultIfEmpty(e.getMessage(), "UNKNOWN ERROR:" + e.getStatus()));
        if (result != null) {
            response.setStatus(HttpStatus.OK.value());
            return result;
        }
        response.setStatus(HttpStatus.OK.value());
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
    public Response<?> DataAccessException(HttpServletRequest request, HttpServletResponse response,
                                           DataAccessException e) {
        logger.error(e.getMessage(), e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (e instanceof DuplicateKeyException) {
            return Response.error(ErrType.INTERNAL_SERVER_ERROR, "数据重复");
        } else {
            return Response.error(ErrType.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
