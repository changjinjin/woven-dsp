package com.info.baymax.common.comp.swagger.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Configuration
@ConditionalOnExpression(value = "!'${swagger2.resources}'.isEmpty()")
@EnableConfigurationProperties(Swagger2Properties.class)
public class SwaggerResourcesProviderAutoConfiguration {

    private final Swagger2Properties properties;

    public SwaggerResourcesProviderAutoConfiguration(Swagger2Properties properties) {
        this.properties = properties;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return new CustomSwaggerResourcesProvider(properties.getResources());
    }

}
