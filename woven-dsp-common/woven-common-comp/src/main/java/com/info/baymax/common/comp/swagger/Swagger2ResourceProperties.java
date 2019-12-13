/*******************************************************************************
 * Copyright 2019 www.inforefiner.com Inc. All rights reserved.
 ******************************************************************************/
package com.info.baymax.common.comp.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;

/**
 * Swagger2配置信息
 *
 * @author: jingwei.yang
 * @date: 2019年4月18日 上午11:49:49
 */
@Getter
@Setter
@Configuration
@ConditionalOnProperty(prefix = Swagger2ResourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConfigurationProperties(prefix = Swagger2ResourceProperties.PREFIX)
public class Swagger2ResourceProperties {
    public static final String PREFIX = "swagger2";

    /**
     * 是否开启文档，可根据发布环境动态配置，如在不同的profiles中设置开启与关闭，生产环境关闭，测试环境和开发环境开放等
     */
    private boolean enabled = true;

    /**
     * SwaggerResource 定义列表
     */
    private List<SwaggerResource> resources;

}
