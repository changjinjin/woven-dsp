package com.jusfoun.services.auth.server.oauth2.kaptcha.generate;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 图片验证码信息对象
 * 
 * @author CaiRui
 * @Date 2018/12/9 18:03
 */
@ApiModel
public class ImageKaptchaVo extends ImageKaptcha {
	private static final long serialVersionUID = 482959520768872900L;

	/**
	 * 验证码图片
	 */
	@JsonIgnore
	private BufferedImage image;

	/**
	 * 图片验证码
	 */
	@ApiModelProperty("base64编码的图片数据")
	private String imageText;

	public ImageKaptchaVo(String uuid, String code, BufferedImage image, int expireAfterSeconds) {
		this(uuid, code, image, LocalDateTime.now().plusSeconds(expireAfterSeconds));
	}

	public ImageKaptchaVo(String uuid, String code, BufferedImage image, LocalDateTime expireTime) {
		super(uuid, code, expireTime);
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public String getImageText() {
		try {
			if (image != null) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				Encoder encoder = Base64.getEncoder();
				ImageIO.write(image, "jpg", outputStream);
				imageText = "data:image/jpg;base64," + encoder.encodeToString(outputStream.toByteArray());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageText;
	}

	public void setImageText(String imageText) {
		this.imageText = imageText;
	}

}