package com.info.baymax.common.swagger.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
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
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = Swagger2Properties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(Swagger2Properties.class)
public class Swagger2Config {
	private final Swagger2Properties properties;

	public Swagger2Config(Swagger2Properties properties) {
		this.properties = properties;
	}

	@Autowired
	@Nullable
	private ReactiveMode reactiveMode;

	@Value("${server.reactive.context-path:${server.servlet.context-path:/}}")
	private String reactiveContextPath;

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

		if (StringUtils.isNotEmpty(reactiveContextPath) && !reactiveContextPath.equals("/") && reactiveMode != null) {
			docket.pathMapping(reactiveContextPath);
		} else {
			docket.pathMapping("/");
		}
        return docket;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????,??????<br>
     * 1????????????????????????????????????????????????token????????? <br>
     * 2??????????????????????????????????????????????????????????????????????????????????????????????????????????????????<br>
     * 3????????????????????????????????????????????????<br>
     *
     * @return ????????????????????????
     */
    private List<Parameter> globalOperationParameters() {
        return properties.parseGlobalParameters();
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = properties.getApiInfo();
        return apiInfo != null ? apiInfo
            : new ApiInfoBuilder()//
            .version("1.0")//
            .title("Baymax??????????????????")//
            .description("Baymax??????????????????")//
            .termsOfServiceUrl("http://www.inforefiner.com/")//
            .contact(new Contact("????????????", "http://www.inforefiner.com/", "business@inforefiner.com"))//
            .build();
    }

    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    // ??????????????????
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

    // ???????????????????????????????????????
    public ResponseMessage buildFailResponseMessage(HttpStatus httpStatus) {
        return new ResponseMessageBuilder()//
            .code(httpStatus.value())//
            .message(httpStatus.getReasonPhrase())//
            .responseModel(new ModelRef("Response"))//
            .examples(Arrays
                .asList(new Example(APPLICATION_JSON_UTF8_VALUE, "{ \"status\": 0, \"message\": \"??????????????????\"}")))//
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
                    List<SwaggerResource> resources = properties.getResources();
                    if (resources != null && resources.size() > 0) {
                        return resources;
                    }

                    SwaggerResource resource = new SwaggerResource();
                    resource.setName("default");
                    resource.setUrl("/v2/api-docs");
                    resource.setSwaggerVersion("1.0");
                    return Arrays.asList(resource);
                }
            };
        }
    }

	@Configuration
	@ConditionalOnClass(name = "org.springframework.web.reactive.BindingContext")
	@EnableSwagger2WebFlux
	public class EnableSwagger2WebFluxAutoConfiguration {
		@Bean
		public ReactiveMode reactiveMode() {
			return new ReactiveMode();
		}
	}

	@Configuration
	@ConditionalOnClass(name = "javax.servlet.http.HttpServletRequest")
	@EnableSwagger2WebMvc
	public class EnableSwagger2WebMvcAutoConfiguration {
	}

	public static final class ReactiveMode {
	}
}
