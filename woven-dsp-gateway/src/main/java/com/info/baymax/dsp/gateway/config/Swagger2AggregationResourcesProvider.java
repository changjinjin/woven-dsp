package com.info.baymax.dsp.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;

/**
 * Swagger 聚合文档提供器，这里用于映射网关和网关代理的服务中swagger文档API，便于系统文档统一查看和管理
 *
 * @author: jingwei.yang
 * @date: 2019年4月18日 上午11:49:24
 */
@Primary
@Component
@ConditionalOnBean(Swagger2ResourceProperties.class)
@ConditionalOnClass(Swagger2ResourceProperties.class)
@ConditionalOnExpression("!'${swagger2.resources}'.isEmpty()")
public class Swagger2AggregationResourcesProvider implements SwaggerResourcesProvider {

    @Autowired
    private Swagger2ResourceProperties resourceProperties;

    @Override
    public List<SwaggerResource> get() {
        return resourceProperties.getResources();
    }

    public SwaggerResource swaggerResource(String serviceName, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(serviceName);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
