package com.jusfoun.services.auth.server.oauth2.kaptcha.generate;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

public class ImageKaptcha implements Serializable {
	private static final long serialVersionUID = 2291350291970005737L;

	/**
	 * 唯一标识
	 */
	@ApiModelProperty("图片验证码标示")
	private String uuid;

	/**
	 * 验证码失效时间
	 */
	@ApiModelProperty("验证码失效时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime expireTime;

	/**
	 * 验证码
	 */
	@ApiModelProperty("验证码")
	@JsonIgnore
	private String code;

	public ImageKaptcha() {
	}

	public ImageKaptcha(String uuid, String code, LocalDateTime expireTime) {
		this.uuid = uuid;
		this.code = code;
		this.expireTime = expireTime;
	}

	/**
	 * 是否失效
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isExpried() {
		return LocalDateTime.now().isAfter(expireTime);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public String toString() {
		return "ImageCaptcha [uuid=" + uuid + ", expireTime=" + expireTime + ", code=" + code + "]";
	}

}
