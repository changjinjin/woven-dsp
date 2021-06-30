package com.info.baymax.common.elasticsearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = MybatisElasticsearchJdbcClientProperties.PREFIX)
public class MybatisElasticsearchJdbcClientProperties {
	public static final String PREFIX = "spring.data.elasticsearch.client.mybatis";

	/**
	 * 自动创建索引
	 */
	public boolean autoCreateIndex = true;

	/**
	 * 创建索引的bean的包路径
	 */
	public String[] basePackages = {};
}
