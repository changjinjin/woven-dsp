package com.merce.woven.dsp.auth.oauth2.exception;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.www.NonceExpiredException;

import com.merce.woven.common.message.result.ErrType;
import com.merce.woven.common.message.result.Response;

public class CustomOauth2ExceptionHandler {

	public Response<?> handle(AuthenticationException e) {
		Response<?> result = null;
		if (e instanceof AccountExpiredException) {
			result = Response.error(ErrType.UNAUTHORIZED, "账户过期或锁定");
		} else if (e instanceof CredentialsExpiredException) {
			result = Response.error(ErrType.UNAUTHORIZED, "登录凭证失效");
		} else if (e instanceof DisabledException) {
			result = Response.error(ErrType.UNAUTHORIZED, "账户已停用，请联系管理员");
		} else if (e instanceof LockedException) {
			result = Response.error(ErrType.UNAUTHORIZED, "账户锁定或失效，请联系管理员");
		} else if (e instanceof AuthenticationCredentialsNotFoundException) {
			result = Response.error(ErrType.UNAUTHORIZED, "无效凭证");
		} else if (e instanceof InternalAuthenticationServiceException) {
			result = Response.error(ErrType.UNAUTHORIZED, "账户内部认证出错");
		} else if (e instanceof BadCredentialsException) {
			result = Response.error(ErrType.UNAUTHORIZED, "用户名或密码错误");
		} else if (e instanceof AccessTokenRequiredException) {
			result = Response.error(ErrType.UNAUTHORIZED, "请求失败，缺少access_token信息");
		} else if (e instanceof UnapprovedClientAuthenticationException) {
			result = Response.error(ErrType.UNAUTHORIZED, "请求失败，客户端授权无效");
		} else if (e instanceof InsufficientAuthenticationException) {
			result = Response.error(ErrType.UNAUTHORIZED, "认证信息缺失，拒绝登录");
		} else if (e instanceof NonceExpiredException) {
			result = Response.error(ErrType.UNAUTHORIZED, "授权摘要失效，请重新获取");
		} else if (e instanceof PreAuthenticatedCredentialsNotFoundException) {
			result = Response.error(ErrType.UNAUTHORIZED, "前置凭证失效，请重新获取");
		} else if (e instanceof ProviderNotFoundException) {
			result = Response.error(ErrType.UNAUTHORIZED, "授权失败");
		} else if (e instanceof CookieTheftException) {
			result = Response.error(ErrType.UNAUTHORIZED, "无效的Cookie");
		} else if (e instanceof InvalidCookieException) {
			result = Response.error(ErrType.UNAUTHORIZED, "无效的Cookie");
		} else if (e instanceof SessionAuthenticationException) {
			result = Response.error(ErrType.UNAUTHORIZED, "错误的会话");
		} else if (e instanceof UsernameNotFoundException) {
			result = Response.error(ErrType.UNAUTHORIZED, "用户名错误");
		} else if (e instanceof RememberMeAuthenticationException) {
			result = Response.error(ErrType.UNAUTHORIZED, "认证失败");
		} else if (e instanceof AccountStatusException) {
			result = Response.error(ErrType.UNAUTHORIZED, "账户状态异常");
		} else if (e instanceof AuthenticationServiceException) {
			result = Response.error(ErrType.UNAUTHORIZED, "账户认证失败");
		} else {
			result = Response.error(ErrType.UNAUTHORIZED, "请求未授权");
		}
		return result;
	}
}
