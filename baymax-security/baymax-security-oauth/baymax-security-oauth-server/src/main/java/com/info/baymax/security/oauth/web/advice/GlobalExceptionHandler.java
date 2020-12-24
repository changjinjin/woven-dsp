package com.info.baymax.security.oauth.web.advice;

import com.info.baymax.common.queryapi.exception.BizException;
import com.info.baymax.common.queryapi.result.ErrMsg;
import com.info.baymax.common.queryapi.result.ErrType;
import com.info.baymax.common.queryapi.result.Response;
import com.info.baymax.security.oauth.api.exception.CustomOauth2Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 说明： 系统异常统一处理. <br>
 *
 * @author jingwei.yang
 * @date 2017年9月9日 下午2:10:32
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody // 在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public Response<?> errorHandler(Exception e, HttpServletRequest request, HttpServletResponse response) {
        Response<?> result = null;
        // 自定义异常错误处理
        if (e != null) {
            log.error(e.getMessage(), e);
            if (e instanceof BizException || BizException.class.isAssignableFrom(e.getClass())) {
                BizException e1 = (BizException) e;
                String message = e1.getMessage();
                ErrMsg errMsg = e1.getErrMsg();
                result = Response
                    .error(errMsg.getStatus(), message != null ? message : "未知错误，错误代码：" + errMsg.getStatus())
                    .build();
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            } else if (e instanceof NoHandlerFoundException) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                result = Response.error(ErrType.NOT_FOUND).build();
            } else if (e instanceof AuthenticationException
                || AuthenticationException.class.isAssignableFrom(e.getClass())) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                result = Response.error(ErrType.UNAUTHORIZED).build();
            } else if (e instanceof HttpMessageNotReadableException) {
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                result = Response.error(ErrType.NOT_ACCEPTABLE).build();
            } else if (e instanceof HttpRequestMethodNotSupportedException) {
                response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
                result = Response.error(ErrType.METHOD_NOT_ALLOWED).build();
            } else if (e instanceof MaxUploadSizeExceededException || e instanceof SizeLimitExceededException) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                result = Response.error(ErrType.FILE_MAX_UPLOAD_SIZE_EXCEEDED_ERROR).build();
            } else if (e instanceof MultipartException) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                result = Response.error(ErrType.FILE_IO_READ_ERROR).build();
            } else if (e instanceof OAuth2Exception) {
                CustomOauth2Exception customOauth2Exception = new CustomOauth2Exception((OAuth2Exception) e);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                result = Response.error(customOauth2Exception.getHttpErrorCode(), customOauth2Exception.getSummary())
                    .build();
            }
        }

        if (result != null) {
            return result;
        }
        // 其他异常
        return Response.error(ErrType.INTERNAL_SERVER_ERROR).build();
    }
}
