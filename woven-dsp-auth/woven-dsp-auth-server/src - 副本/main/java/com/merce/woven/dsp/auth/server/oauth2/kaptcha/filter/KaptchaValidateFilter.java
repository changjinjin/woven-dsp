package com.jusfoun.services.auth.server.oauth2.kaptcha.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jusfoun.common.base.cache.CacheConsts;
import com.jusfoun.common.base.constants.AuthConstants;
import com.jusfoun.common.utils.ICollections;
import com.jusfoun.services.auth.server.oauth2.kaptcha.generate.ImageKaptcha;

/**
 * Kaptcha 认证过滤器
 * 
 * @author yjw@jusfoun.com
 * @date 2019-02-21 09:01:11
 */
public class KaptchaValidateFilter extends OncePerRequestFilter {

	/**
	 * 缓存管理器
	 */
	private CacheManager cacheManager;

	/**
	 * 异常处理器
	 */
	private AuthenticationFailureHandler failureHandler;

	/**
	 * 需要拦截的访问路径
	 */
	private Set<String> interceptUrls;

	public KaptchaValidateFilter(CacheManager cacheManager, AuthenticationFailureHandler failureHandler) {
		this.cacheManager = cacheManager;
		this.failureHandler = failureHandler;
	}

	public KaptchaValidateFilter(CacheManager cacheManager, AuthenticationFailureHandler failureHandler,
			Set<String> interceptUrls) {
		this(cacheManager, failureHandler);
		this.interceptUrls = interceptUrls;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String currRequestURI = request.getRequestURI();
		if ((ICollections.hasElements(interceptUrls) && interceptUrls.contains(currRequestURI))
				|| (ICollections.hasNoElements(interceptUrls)
						&& StringUtils.equals(AuthConstants.TOKEN_ENTRY_POINT, currRequestURI)
						&& StringUtils.equalsIgnoreCase("POST", request.getMethod()))) {
			try {
				validate(request);
			} catch (KaptchaException e) {
				failureHandler.onAuthenticationFailure(request, response, e);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * 图片验证码校验
	 *
	 * @param request
	 */
	private void validate(HttpServletRequest request) throws ServletRequestBindingException {
		String codeInRequest = ServletRequestUtils.getStringParameter(request, "imageCode");
		if (StringUtils.isBlank(codeInRequest)) {
			throw new KaptchaException("验证码的值不能为空");
		}

		String uuidInRequest = ServletRequestUtils.getStringParameter(request, "uuid");

		// 拿到之前存储的imageCode信息
		ImageKaptcha imageCodeInCache = null;
		Cache cache = cacheManager.getCache(CacheConsts.CACHE_CAPTCHA);
		try {
			imageCodeInCache = (ImageKaptcha) cache.get(uuidInRequest).get();
		} catch (Exception e) {
			imageCodeInCache = null;
		}

		try {
			if (imageCodeInCache == null) {
				throw new KaptchaException("验证码不存在");
			}

			if (imageCodeInCache.isExpried()) {
				throw new KaptchaException("验证码已过期");
			}

			if (!StringUtils.equals(imageCodeInCache.getCode(), codeInRequest)) {
				throw new KaptchaException("验证码不匹配");
			}
		} finally {
			// 不管通不通过验证，都把该uuid对应的值从缓存移除
			cache.evict(uuidInRequest);
		}
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}

	public void setInterceptUrls(Set<String> interceptUrls) {
		this.interceptUrls = interceptUrls;
	}
}
