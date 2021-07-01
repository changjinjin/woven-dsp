package com.info.baymax.common.elasticsearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;

@Configuration
public class ElasticsearchClientConfig {

	@Bean
	public SnakeCaseFieldNamingStrategy fieldNamingStrategy() {
		return new SnakeCaseFieldNamingStrategy();
	}
}
