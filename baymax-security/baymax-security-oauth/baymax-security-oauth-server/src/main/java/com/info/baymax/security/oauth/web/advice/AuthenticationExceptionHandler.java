package com.info.baymax.security.oauth.web.advice;

import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Nullable;

/**
 * 说明： 系统异常统一处理. <br>
 *
 * @author jingwei.yang
 * @date 2017年9月9日 下午2:10:32
 */
@Slf4j
@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @Autowired
    @Nullable
    private MessageSourceAccessor messageSourceAccessor;

    @ResponseBody
    @ExceptionHandler(value = {AuthenticationException.class, OAuth2Exception.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @Order(-5)
    public Response<?> authenticationException(Exception e) {
        log.error(e.getMessage(), e);
        return Response.error(ErrType.UNAUTHORIZED, e.getMessage()).details(e.toString()).build();
    }

    @ResponseBody
    @ExceptionHandler(value = {AccessDeniedException.class, OAuth2AccessDeniedException.class,
        UserDeniedAuthorizationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @Order(-6)
    public Response<?> accessDeniedException(Exception e) {
        log.error(e.getMessage(), e);
        return Response.error(ErrType.FORBIDDEN, e.getMessage()).details(e.toString()).build();
    }
}
