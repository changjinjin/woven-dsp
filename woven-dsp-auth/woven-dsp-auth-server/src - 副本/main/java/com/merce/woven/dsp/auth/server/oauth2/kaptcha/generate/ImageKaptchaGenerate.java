package com.jusfoun.services.auth.server.oauth2.kaptcha.generate;

import java.awt.image.BufferedImage;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.kaptcha.Producer;

/**
 * 图片验证码生成
 * 
 * @author yjw@jusfoun.com
 * @date 2019-02-21 09:35:45
 */
@Service
public class ImageKaptchaGenerate implements KaptchaGenerate {

	/**
	 * 图片生成器，config bean中配置
	 */
	@Autowired
	private Producer producer;

	@Override
	public ImageKaptchaVo generate(String uuid, int expireSecondes) {
		String code = producer.createText();
		BufferedImage bufferedImage = producer.createImage(code);
		return new ImageKaptchaVo(StringUtils.isEmpty(uuid) ? UUID.randomUUID().toString() : uuid, code, bufferedImage,
				expireSecondes == 0 ? 60 * 10 : expireSecondes);// 默认10分钟过期
	}

}