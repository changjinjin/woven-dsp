package com.info.baymax.dsp.gateway.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.swagger.web.SwaggerResource;

@Getter
@Setter
@ConfigurationProperties(prefix = "swagger2")
public class Swagger2Properties {
	/**
	 * 是否开启文档，可根据发布环境动态配置，如在不同的profiles中设置开启与关闭，生产环境关闭，测试环境和开发环境开放等
	 */
	private boolean enabled = true;

	/**
	 * SwaggerResource 定义列表
	 */
	private List<SwaggerResource> resources;

}
