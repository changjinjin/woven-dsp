package com.jusfoun.services.auth.server.web.controller.kaptcha;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jusfoun.common.base.cache.CacheConsts;
import com.jusfoun.common.message.result.BaseResponse;
import com.merce.woven.dsp.auth.server.oauth2.kaptcha.KaptchaProperties;
import com.merce.woven.dsp.auth.server.oauth2.kaptcha.generate.ImageKaptcha;
import com.merce.woven.dsp.auth.server.oauth2.kaptcha.generate.ImageKaptchaVo;
import com.merce.woven.dsp.auth.server.oauth2.kaptcha.generate.KaptchaGenerate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Kaptcha相关接口
 * 
 * @author yjw@jusfoun.com
 * @date 2019-02-21 09:51:27
 */
@Api(tags = "认证管理", value = "系统用户认证管理", description = "系统用户认证管理")
@RestController
public class KaptchaController {

	@Autowired
	private KaptchaGenerate kaptchaGenerate;

	@Autowired
	private KaptchaProperties kaptchaProperties;

	@Autowired
	private CacheManager cacheManager;

	@ApiOperation(value = "图片验证码生成", notes = "图片验证码生成", hidden = false)
	@GetMapping("/kaptcha")
	@ResponseBody
	public BaseResponse<ImageKaptchaVo> kaptcha(//
			HttpServletResponse response, //
			@ApiParam(value = "验证码标示，后台生成，首次请求为空，以后取第一次返回的uuid值，验证码刷新时需要") @RequestParam(required = false) String uuid //
	) {
		// 生成验证码
		ImageKaptchaVo imageKaptchaVo = kaptchaGenerate.generate(uuid, kaptchaProperties.getExpireAfterSecondes());

		// 必要属性放入缓存中
		ImageKaptcha imageKaptcha = new ImageKaptcha(imageKaptchaVo.getUuid(), imageKaptchaVo.getCode(),
				imageKaptchaVo.getExpireTime());
		Cache cache = cacheManager.getCache(CacheConsts.CACHE_CAPTCHA);
		cache.put(imageKaptchaVo.getUuid(), imageKaptcha);
		System.out.println(imageKaptcha);
		return BaseResponse.success(imageKaptchaVo);
	}
}
