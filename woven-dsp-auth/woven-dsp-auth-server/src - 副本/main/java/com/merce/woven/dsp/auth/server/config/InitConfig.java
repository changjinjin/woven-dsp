package com.merce.woven.dsp.auth.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 说明：模板配置信息. <br>
 * 
 * @author yjw@jusfoun.com
 * @date 2018年6月15日 上午9:22:09
 */
@Configuration
@ConfigurationProperties(prefix = InitConfig.PREFIX)
public class InitConfig {
	public static final String PREFIX = "system.init";

	/**
	 * 是否开启系统初始化
	 */
	private boolean enable;

	/**
	 * 初始化账户
	 */
	private String username;

	/**
	 * 初始化密码
	 */
	private String password;

	/**
	 * 系统权限初始化文件
	 */
	private String sysPrivssFile;

	/**
	 * 客户端信息初始化文件
	 */
	private String clientDetailsFile;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSysPrivssFile() {
		return sysPrivssFile;
	}

	public void setSysPrivssFile(String sysPrivssFile) {
		this.sysPrivssFile = sysPrivssFile;
	}

	public String getClientDetailsFile() {
		return clientDetailsFile;
	}

	public void setClientDetailsFile(String clientDetailsFile) {
		this.clientDetailsFile = clientDetailsFile;
	}

}
