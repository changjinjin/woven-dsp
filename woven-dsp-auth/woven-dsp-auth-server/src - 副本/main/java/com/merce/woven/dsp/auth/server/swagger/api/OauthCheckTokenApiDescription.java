package com.merce.woven.dsp.auth.server.swagger.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import com.jusfoun.common.base.constants.AuthConstants;

import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Parameter;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

/**
 * /oauth/check_token 接口文档
 * 
 * @author yjw
 * @date 2019年1月27日 上午10:42:51
 */
public class OauthCheckTokenApiDescription implements SwaggerAdditionApiDescription {

	@Override
	public ApiDescription get() {
		return new ApiDescription(AuthConstants.TOKEN_CHECK_ENTRY_POINT, // url
				"检查token端点", // 描述
				Arrays.asList(new OperationBuilder(new CachingOperationNameGenerator())//
						.method(HttpMethod.POST)// http请求类型
						.produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))//
						.summary("检查token是否有效")//
						.notes("检查token是否有效")// 方法描述
						.tags(Sets.newHashSet("认证管理"))// 归类标签
						.parameters(parameters())//
						.build()),
				false);
	}

	private List<Parameter> parameters() {
		return Arrays.asList(new ParameterBuilder()//
				.description("令牌")//
				.type(new TypeResolver().resolve(String.class))//
				.name("token")//
				.parameterType("query")//
				.parameterAccess("access")//
				.required(true)//
				.modelRef(new ModelRef("string")) //
				.build());
	}

}
