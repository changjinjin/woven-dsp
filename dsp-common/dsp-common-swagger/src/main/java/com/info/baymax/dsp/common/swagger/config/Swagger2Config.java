package com.info.baymax.dsp.common.swagger.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.Example;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = Swagger2Properties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(Swagger2Properties.class)
public class Swagger2Config {
    private final Swagger2Properties properties;

    public Swagger2Config(Swagger2Properties properties) {
        this.properties = properties;
    }

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)//
            .enable(true)//
            .apiInfo(apiInfo())//
            .select()//
            .apis(RequestHandlerSelectors
                .basePackage(StringUtils.defaultString(properties.getBasePackage(), "com.info.baymax")))//
            .paths(PathSelectors.any())//
            .build() //
            .globalOperationParameters(globalOperationParameters())//
            .globalResponseMessage(RequestMethod.GET, responseMessages)//
            .globalResponseMessage(RequestMethod.GET, responseMessages);//
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

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = properties.getApiInfo();
        return apiInfo != null ? apiInfo
            : new ApiInfoBuilder()//
            .version("1.0")//
            .title("Baymax平台接口文档")//
            .description("Baymax平台接口文档")//
            .termsOfServiceUrl("http://www.inforefiner.com/")//
            .contact(new Contact("睿帆科技", "http://www.inforefiner.com/", "business@inforefiner.com"))//
            .build();
    }

    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    // 公共响应信息
    ArrayList<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>() {
        private static final long serialVersionUID = 2010798500251777550L;

        {
            add(buildFailResponseMessage(HttpStatus.OK));
            add(buildFailResponseMessage(HttpStatus.UNAUTHORIZED));
            add(buildFailResponseMessage(HttpStatus.FORBIDDEN));
            add(buildFailResponseMessage(HttpStatus.NOT_FOUND));
            add(buildFailResponseMessage(HttpStatus.METHOD_NOT_ALLOWED));
            add(buildFailResponseMessage(HttpStatus.NOT_ACCEPTABLE));
            add(buildFailResponseMessage(HttpStatus.PROXY_AUTHENTICATION_REQUIRED));
            add(buildFailResponseMessage(HttpStatus.REQUEST_TIMEOUT));
            add(buildFailResponseMessage(HttpStatus.CONFLICT));
            add(buildFailResponseMessage(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
            add(buildFailResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR));
            add(buildFailResponseMessage(HttpStatus.NOT_IMPLEMENTED));
            add(buildFailResponseMessage(HttpStatus.BAD_GATEWAY));
            add(buildFailResponseMessage(HttpStatus.SERVICE_UNAVAILABLE));
        }
    };

    // 构建一个响应错误的消息信息
    public ResponseMessage buildFailResponseMessage(HttpStatus httpStatus) {
        return new ResponseMessageBuilder()//
            .code(httpStatus.value())//
            .message(httpStatus.getReasonPhrase())//
            .responseModel(new ModelRef("Response"))//
            .examples(Arrays
                .asList(new Example(APPLICATION_JSON_UTF8_VALUE, "{ \"status\": 0, \"message\": \"请求处理成功\"}")))//
            .build();
    }

    @Configuration
    @ConditionalOnExpression(value = "!'${swagger2.resources}'.isEmpty()")
    public class SwaggerResourcesProviderAutoConfiguration {
        @Primary
        @Bean
        public SwaggerResourcesProvider swaggerResourcesProvider() {
            return new SwaggerResourcesProvider() {
                @Override
                public List<SwaggerResource> get() {
                    return properties.getResources();
                }
            };
        }
    }
}
