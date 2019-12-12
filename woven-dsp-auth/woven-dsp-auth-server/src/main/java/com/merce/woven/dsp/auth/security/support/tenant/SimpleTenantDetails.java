package com.merce.woven.dsp.auth.security.support.tenant;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端实体简单实现
 *
 * @author: jingwei.yang
 * @date: 2019年4月22日 下午2:05:31
 */
@Getter
@Setter
public class SimpleTenantDetails implements TenantDetails {

	/**
	 * 客户端ID
	 */
	private String clientId;

	/**
	 * 租户名称
	 */
	private String tenant;

	/**
	 * 系统版本号
	 */
	private String version;

	public SimpleTenantDetails() {
	}

	public SimpleTenantDetails(String clientId, String tenant, String version) {
		super();
		this.clientId = clientId;
		this.tenant = tenant;
		this.version = version;
	}
	
	
}
