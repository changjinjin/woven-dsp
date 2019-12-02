package com.jusfoun.services.auth.server.oauth2.kaptcha;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 图片验证码配置项
 * 
 * @author yjw@jusfoun.com
 * @date 2019-02-18 14:17:59
 */
@Configuration
@ConfigurationProperties(prefix = KaptchaProperties.PREFIX)
public class KaptchaProperties {
	public static final String PREFIX = "kaptcha.image";

	/**
	 * 验证码长度,默认4位
	 */
	private int size = 5;

	/**
	 * 宽度，默认110
	 */
	private int width = 100;

	/**
	 * 高度，默认34
	 */
	private int height = 34;

	/**
	 * 失效秒数，默认5分钟
	 */
	private int expireAfterSecondes = 5 * 60;

	/**
	 * 验证码拦截的路径 多个路径以,(逗号)进行分割
	 */
	private Set<String> interceptUrls;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getExpireAfterSecondes() {
		return expireAfterSecondes;
	}

	public void setExpireAfterSecondes(int expireAfterSecondes) {
		this.expireAfterSecondes = expireAfterSecondes;
	}

	public Set<String> getInterceptUrls() {
		return interceptUrls;
	}

	public void setInterceptUrls(Set<String> interceptUrls) {
		this.interceptUrls = interceptUrls;
	}
}
