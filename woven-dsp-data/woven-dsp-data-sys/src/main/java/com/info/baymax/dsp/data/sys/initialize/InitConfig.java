package com.info.baymax.dsp.data.sys.initialize;

import lombok.Data;

/**
 * 说明：模板配置信息. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2018年6月15日 上午9:22:09
 */
@Data
public class InitConfig {

	/**
	 * 是否开启系统初始化
	 */
	private boolean enable = true;

	/**
	 * 初始化账户
	 */
	private String username = "admin";

	/**
	 * 初始化密码
	 */
	private String password = "123456";

	/**
	 * 密码格式检查是否使用严格模式
	 */
	private boolean pwdStrict = false;

	/**
	 * 系统权限初始化文件
	 */
	private String permsFile;

}
