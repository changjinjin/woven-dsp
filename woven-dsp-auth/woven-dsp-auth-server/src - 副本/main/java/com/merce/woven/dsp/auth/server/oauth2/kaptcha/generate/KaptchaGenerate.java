package com.jusfoun.services.auth.server.oauth2.kaptcha.generate;

/**
 * 验证码生成接口
 * 
 * @author yjw@jusfoun.com
 * @date 2019-02-18 14:11:15
 */
public interface KaptchaGenerate {

	/**
	 * 验证码图片生成
	 * 
	 * @author yjw@jusfoun.com
	 * @date 2019-02-21 09:33:35
	 * @param uuid
	 *            绑定唯一标示，第一次为空
	 * @param expireSecondes
	 *            过期秒数
	 * @return 图片验证码包装对象
	 */
	ImageKaptchaVo generate(String uuid, int expireSecondes);
}
