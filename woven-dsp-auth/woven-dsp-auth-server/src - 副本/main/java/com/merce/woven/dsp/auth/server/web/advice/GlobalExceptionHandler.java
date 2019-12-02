package com.jusfoun.services.auth.server.web.advice;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.merce.woven.common.message.exception.BizException;
import com.merce.woven.common.message.result.ErrType;
import com.merce.woven.common.message.result.Response;
import com.merce.woven.dsp.auth.server.oauth2.exception.CustomOauth2ExceptionHandler;

/**
 * 说明： 系统异常统一处理. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年9月9日 下午2:10:32
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseBody // 在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
	public Response<?> exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) {
		Response<?> result = null;
		// 自定义异常错误处理
		if (e != null) {
			e.printStackTrace();
			if (e instanceof BizException || BizException.class.isAssignableFrom(e.getClass())) {
				BizException e1 = (BizException) e;
				String message = e1.getMessage();
				result = Response.error(e1.getStatus(), message != null ? message : "未知错误，错误代码：" + e1.getStatus());
			} else if (e instanceof NoHandlerFoundException) {
				result = Response.error(ErrType.NOT_FOUND);
			} else if (e instanceof AuthenticationException
					|| AuthenticationException.class.isAssignableFrom(e.getClass())) {
				result = Response.error(ErrType.UNAUTHORIZED);
			} else if (e instanceof HttpMessageNotReadableException) {
				result = Response.error(ErrType.NOT_ACCEPTABLE);
			} else if (e instanceof HttpRequestMethodNotSupportedException) {
				result = Response.error(ErrType.METHOD_NOT_ALLOWED);
			} else if (e instanceof MaxUploadSizeExceededException || e instanceof SizeLimitExceededException) {
				result = Response.error(ErrType.FILE_MAX_UPLOAD_SIZE_EXCEEDED_ERROR);
			} else if (e instanceof MultipartException) {
				result = Response.error(ErrType.FILE_IO_READ_ERROR);
			} else if (e instanceof AuthenticationException) {
				CustomOauth2ExceptionHandler handler = new CustomOauth2ExceptionHandler();
				response.setStatus(ErrType.UNAUTHORIZED.getStatus());
				result = handler.handle((AuthenticationException) e);
			}
		}

		if (result != null) {
			return result;
		}
		// 其他异常
		return Response.error(ErrType.INTERNAL_SERVER_ERROR);
	}
}
