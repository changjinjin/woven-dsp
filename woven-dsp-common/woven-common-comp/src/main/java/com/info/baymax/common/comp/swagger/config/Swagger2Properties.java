package com.info.baymax.common.comp.swagger.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.swagger.web.SwaggerResource;

/**
 * Swagger2配置信息
 *
 * @author: jingwei.yang
 * @date: 2019年4月18日 上午11:49:49
 */
@Getter
@Setter
@ConfigurationProperties(prefix = Swagger2Properties.PREFIX)
public class Swagger2Properties {
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
