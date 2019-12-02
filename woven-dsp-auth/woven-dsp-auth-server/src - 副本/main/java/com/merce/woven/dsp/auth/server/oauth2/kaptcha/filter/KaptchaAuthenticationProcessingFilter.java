package com.jusfoun.services.auth.server.oauth2.kaptcha.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jusfoun.common.base.cache.CacheConsts;
import com.jusfoun.common.message.result.BaseResponse;
import com.merce.woven.common.message.result.ErrType;
import com.merce.woven.dsp.auth.server.oauth2.kaptcha.generate.ImageKaptcha;
import com.merce.woven.dsp.common.constants.AuthConstants;

/**
 * Kaptcha认证过滤器
 * 
 * @author yjw@jusfoun.com
 * @date 2019-02-21 09:31:41
 */
public class KaptchaAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();

	private String imageCodeParameter = "imageCode";
	private String uuidParameter = "uuid";
	private String usernameParameter = "username";
	private String passwordParameter = "password";
	private boolean postOnly = true;

	private final CacheManager cacheManager;

	public KaptchaAuthenticationProcessingFilter(final CacheManager cacheManager) {
		super(new AntPathRequestMatcher(AuthConstants.TOKEN_ENTRY_POINT, "POST"));
		this.cacheManager = cacheManager;
	}

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		// 获取验证码信息
		String uuid = obtainUuid(request);
		String imageCode = obtainImageCode(request);

		if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(imageCode)) {
			throw new KaptchaException("验证码错误");
		}

		validateImageCode(uuid, imageCode);

		// 获取用户名密码信息
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}
		username = username.trim();

		// CaptchaAuthenticationToken authRequest = new
		// CaptchaAuthenticationToken(username, password, uuid, imageCode);

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	private void validateImageCode(String uuid, String imageCode) throws KaptchaException {
		// 从缓存中拿出验证码
		ImageKaptcha imageCaptcha = null;
		Cache cache = cacheManager.getCache(CacheConsts.CACHE_CAPTCHA);
		try {
			imageCaptcha = (ImageKaptcha) cache.get(uuid.trim()).get();
		} catch (Exception e) {
			imageCaptcha = null;
		}

		if (imageCaptcha == null) {
			throw new KaptchaException("验证码错误");
		}

		if (imageCaptcha.isExpried()) {
			cache.evict(uuid);
			throw new KaptchaException("验证码已过期");
		}

		if (!StringUtils.equalsIgnoreCase(imageCaptcha.getCode(), imageCode)) {
			throw new KaptchaException("验证码错误");
		}
		// 验证通过 移除缓存
		cache.evict(uuid);
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				SecurityContextHolder.clearContext();
				if (exception instanceof KaptchaException) {
					ObjectMapper objectMapper = new ObjectMapper();
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
					objectMapper.writeValue(response.getWriter(),
							BaseResponse.exception(ErrType.UNAUTHORIZED, "验证码错误"));
				} else {
					authenticationEntryPoint.commence(request, response, exception);
				}
			}
		});
		setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				// no-op - just allow filter chain to continue to token endpoint
			}
		});
	}

	protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}

	protected String obtainImageCode(HttpServletRequest request) {
		return request.getParameter(imageCodeParameter);
	}

	protected String obtainUuid(HttpServletRequest request) {
		return request.getParameter(uuidParameter);
	}

}
