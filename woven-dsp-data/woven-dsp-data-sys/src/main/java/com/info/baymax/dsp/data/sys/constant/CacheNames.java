package com.info.baymax.dsp.data.sys.constant;

/**
 * 缓存名称常量定义
 *
 * @auther jingwei.yang
 * @date 2019/4/28 20:53
 */
public final class CacheNames {
	/**
	 * 临时使用
	 */
	public static final String CACHE_TEMP = "securityCache";

	/**
	 * 安全缓存，需要配合安全策略使用
	 */
	public static final String CACHE_SECURITY = "securityCache";

	/**
	 * 持久性的缓存，缓存一些基本不变化的信息，不需要过期时间
	 */
	public static final String CACHE_PERSISTENT = "persistentCache";
}
