package com.info.baymax.security.oauth.config;

/**
 * 说明： 缓存常量. <br>
 *
 * @author jingwei.yang
 * @date 2017年10月13日 下午2:20:20
 */
public class CacheConsts {
	// 临时缓存：存放较短时间缓存
	public static final String CACHE_TEMP = "tempCache";
	// 临时缓存：安全缓存
	public static final String CACHE_SECURITY = "securityCache";
	// 临时缓存：安全缓存
	public static final String CACHE_CAPTCHA = "captchaCache";
	// 临时缓存：安全缓存
	public static final String CACHE_PRIFIX_SECURITY = "security_cache_token_";
	// 自定义缓存
	public static final String CACHE_CUSTOM = "customCache";
	// 持久缓存
	public static final String CACHE_PERSISTENT = "persistentCache";
}
