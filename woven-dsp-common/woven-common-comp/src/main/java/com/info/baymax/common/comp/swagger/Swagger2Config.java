/*******************************************************************************
 * Copyright 2019 www.inforefiner.com Inc. All rights reserved.
 ******************************************************************************/

package com.info.baymax.common.comp.swagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

/**
 * Swagger2文档配置类，这里用于设置swagger启动相关配置项，在应用中开启Swagger服务
 *
 * @author: jingwei.yang
 * @date: 2019年4月18日 上午11:49:37
 */
@Configuration
@EnableSwagger2WebFlux // 开启Swagger服务，针对WebFlux应用
@ConditionalOnProperty(prefix = Swagger2Properties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class Swagger2Config {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)//
            .enable(true)//
            .apiInfo(apiInfo())//
            .select()//
            .apis(RequestHandlerSelectors.basePackage("com.info.baymax"))//
            .paths(PathSelectors.any())//
            .build() //
            .globalOperationParameters(globalOperationParameters())//
            .globalResponseMessage(RequestMethod.GET, responseMessages)//
            .globalResponseMessage(RequestMethod.POST, responseMessages);//
        if (contextPath != null && contextPath.length() > 0) {
            docket.pathMapping(contextPath);
        }
        return docket;
    }

    /**
     * 添加接口公共的参数配置项信息，这些参数可以统一设置,如：<br>
     * 1）在客户端请求时是必传参数，如：token信息等 <br>
     * 2）参数在接口定义中没有，但是需要客户端传递，通过过滤器或者其他途径拦截的参数<br>
     * 3）一些我们认为可以统一配置的场景<br>
     *
     * @return 公共参数定义列表
     */
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

    /**
     * API信息声明
     *
     * @return API声明信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()//
            .version("1.0")//
            .title("Baymax平台接口文档")//
            .description("Baymax平台接口文档")//
            .termsOfServiceUrl("http://www.inforefiner.com/")//
            .contact(new Contact("睿帆科技", "http://www.inforefiner.com/", "business@inforefiner.com"))//
            .build();
    }

    // 公共响应信息
    ArrayList<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>() {
        private static final long serialVersionUID = 2010798500251777550L;

        {
            add(new ResponseMessageBuilder().code(HttpStatus.OK.value()).message("业务处理成功")
                .responseModel(new ModelRef("BaseResponse"))
                .examples(Arrays.asList(new Example(MediaType.APPLICATION_JSON_UTF8_VALUE, Response.ok())))
                .build());
            add(new ResponseMessageBuilder().code(HttpStatus.OK.value()).message("业务处理失败")
                .responseModel(new ModelRef("BaseResponse"))
                .examples(Arrays
                    .asList(new Example(MediaType.APPLICATION_JSON_UTF8_VALUE, Response.error(ErrType.FAILED))))
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

    // 构建一个响应错误的消息信息
    public ResponseMessage buildFailResponseMessage(HttpStatus httpStatus, ErrType errType) {
        return new ResponseMessageBuilder()//
            .code(httpStatus.value())//
            .message(httpStatus.getReasonPhrase())//
            .responseModel(new ModelRef("Response"))//
            .examples(Arrays.asList(new Example(MediaType.APPLICATION_JSON_UTF8_VALUE, Response.error(errType))))//
            .build();
    }

}
