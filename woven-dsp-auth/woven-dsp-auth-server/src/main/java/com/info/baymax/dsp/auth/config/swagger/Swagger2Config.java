package com.info.baymax.dsp.auth.config.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.info.baymax.common.message.result.ErrType;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", name = "enabled", havingValue = "true", matchIfMissing = true)
public class Swagger2Config {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)//
				.enable(true)//
				.apiInfo(apiInfo())//
				.select()//
				.apis(RequestHandlerSelectors.basePackage("com.info.baymax"))//
				.paths(PathSelectors.any())//
				.build() //
				.globalOperationParameters(globalOperationParameters())//
				.globalResponseMessage(RequestMethod.GET, responseMessages)//
				.globalResponseMessage(RequestMethod.POST, responseMessages);//
	}

	private List<Parameter> globalOperationParameters() {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new ParameterBuilder()//
				.name("AUTHORIZATION")//
				.description("请求令牌")//
				.modelRef(new ModelRef("string"))//
				.parameterType("header")//
				.required(false)//
				.defaultValue("Bearer ")//
				.allowEmptyValue(true)//
				.order(Ordered.LOWEST_PRECEDENCE)//
				.build()//
		);
		return parameters;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()//
				.version("1.0")//
				.title("Baymax平台接口文档")//
				.description("Baymax平台接口文档")//
				.termsOfServiceUrl("http://www.inforefiner.com/")//
				.contact(new Contact("睿帆科技", "http://www.inforefiner.com/", "business@inforefiner.com"))//
				.build();
	}

	ArrayList<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>() {
		private static final long serialVersionUID = 2010798500251777550L;

		{
			add(new ResponseMessageBuilder().code(HttpStatus.OK.value()).message("业务处理成功")
					.responseModel(new ModelRef("BaseResponse"))
//					.examples(Arrays.asList(new Example(MediaType.APPLICATION_JSON_UTF8_VALUE, Response.ok())))
					.build());
			add(new ResponseMessageBuilder().code(HttpStatus.OK.value()).message("业务处理失败")
					.responseModel(new ModelRef("BaseResponse"))
//					.examples(Arrays.asList(new Example(MediaType.APPLICATION_JSON_UTF8_VALUE, Response.error(ErrType.FAILED))))
					.build());
			add(new ResponseMessageBuilder().code(200).message("请求成功").build());
			add(buildFailResponseMessage(HttpStatus.UNAUTHORIZED, ErrType.UNAUTHORIZED));
			add(buildFailResponseMessage(HttpStatus.FORBIDDEN, ErrType.FORBIDDEN));
			add(buildFailResponseMessage(HttpStatus.NOT_FOUND, ErrType.NOT_FOUND));
			add(buildFailResponseMessage(HttpStatus.METHOD_NOT_ALLOWED, ErrType.METHOD_NOT_ALLOWED));
			add(buildFailResponseMessage(HttpStatus.NOT_ACCEPTABLE, ErrType.NOT_ACCEPTABLE));
			add(buildFailResponseMessage(HttpStatus.PROXY_AUTHENTICATION_REQUIRED,
					ErrType.PROXY_AUTHENTICATION_REQUIRED));
			add(buildFailResponseMessage(HttpStatus.REQUEST_TIMEOUT, ErrType.REQUEST_TIMEOUT));
			add(buildFailResponseMessage(HttpStatus.CONFLICT, ErrType.CONFLICT));
			add(buildFailResponseMessage(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ErrType.UNSUPPORTED_MEDIA_TYPE));
			add(buildFailResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrType.INTERNAL_SERVER_ERROR));
			add(buildFailResponseMessage(HttpStatus.NOT_IMPLEMENTED, ErrType.NOT_IMPLEMENTED));
			add(buildFailResponseMessage(HttpStatus.BAD_GATEWAY, ErrType.BAD_GATEWAY));
			add(buildFailResponseMessage(HttpStatus.SERVICE_UNAVAILABLE, ErrType.SERVICE_UNAVAILABLE));
		}
	};

	public ResponseMessage buildFailResponseMessage(HttpStatus httpStatus, ErrType errType) {
		return new ResponseMessageBuilder()//
				.code(httpStatus.value())//
				.message(httpStatus.getReasonPhrase())//
				.responseModel(new ModelRef("Response"))//
//				.examples(Arrays.asList(new Example(MediaType.APPLICATION_JSON_UTF8_VALUE, Response.error(errType))))//
				.build();
	}
}
