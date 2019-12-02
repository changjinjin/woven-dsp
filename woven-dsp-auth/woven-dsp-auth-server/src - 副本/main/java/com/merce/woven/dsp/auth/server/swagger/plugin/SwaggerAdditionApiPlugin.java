package com.merce.woven.dsp.auth.server.swagger.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.merce.woven.dsp.auth.server.swagger.api.OauthAuthorizeApiDescription;
import com.merce.woven.dsp.auth.server.swagger.api.OauthCheckTokenApiDescription;
import com.merce.woven.dsp.auth.server.swagger.api.OauthTokenApiDescription;
import com.merce.woven.dsp.auth.server.swagger.api.OauthTokenKeyApiDescription;

import springfox.documentation.service.ApiDescription;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;

/**
 * /oauth/token 接口文档
 * 
 * @author yjw
 * @date 2019年1月27日 上午9:57:20
 */
@Component
public class SwaggerAdditionApiPlugin implements ApiListingScannerPlugin {

	@Override
	public List<ApiDescription> apply(DocumentationContext documentationContext) {
		return new ArrayList<ApiDescription>(//
				Arrays.asList(//
						new OauthTokenApiDescription().get(), //
						new OauthAuthorizeApiDescription().get(), //
						new OauthCheckTokenApiDescription().get(), //
						new OauthTokenKeyApiDescription().get() //
				));
	}

	@Override
	public boolean supports(DocumentationType documentationType) {
		return DocumentationType.SWAGGER_2.equals(documentationType);
	}
}