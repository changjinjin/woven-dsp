package com.jusfoun.services.auth.server.oauth2.kaptcha.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import com.jusfoun.common.base.cache.CacheConsts;
import com.jusfoun.services.auth.server.oauth2.kaptcha.generate.ImageKaptcha;

/**
 * 说明： Kaptcha认证提供器. <br>
 * 
 * @author yjw@jusfoun.com
 * @date 2017年11月8日 下午2:48:46
 */
public class KaptchaAuthenticationProvider implements AuthenticationProvider {
	private final CacheManager cacheManager;

	public KaptchaAuthenticationProvider(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");
		KaptchaAuthenticationToken auth = (KaptchaAuthenticationToken) authentication;

		String uuid = auth.getUuid();
		String imageCode = auth.getImageCode();

		validateImageCode(uuid, imageCode);
		// 认证通过
		return auth;
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
	public boolean supports(Class<?> authentication) {
		return (KaptchaAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
