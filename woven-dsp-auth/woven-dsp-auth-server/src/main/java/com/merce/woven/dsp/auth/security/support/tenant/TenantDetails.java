package com.merce.woven.dsp.auth.security.support.tenant;

/**
 * 客户端信息抽象信息
 * 
 * @author: jingwei.yang
 * @date: 2019年4月19日 上午10:30:46
 */
public interface TenantDetails {

	/**
	 * 获取客户端ID
	 * 
	 * @return 客户端ID
	 */
	String getTenant();

	/**
	 * 获取系统版本号
	 * 
	 * @return 系统版本号
	 */
	String getVersion();
}
