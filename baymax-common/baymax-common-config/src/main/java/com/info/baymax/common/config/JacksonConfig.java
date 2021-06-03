package com.info.baymax.common.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.info.baymax.common.config.JacksonConfig.JacksonExtSerializationProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

@Configuration
@EnableConfigurationProperties(JacksonExtSerializationProperties.class)
public class JacksonConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(
		JacksonExtSerializationProperties extProperties) {
		Jackson2ObjectMapperBuilderCustomizer cunstomizer = new Jackson2ObjectMapperBuilderCustomizer() {
			@Override
			public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
				if (extProperties != null && extProperties.isWriteLongAsString()) {
					jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
					jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
					jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
					jacksonObjectMapperBuilder.serializerByType(BigDecimal.class, ToStringSerializer.instance);
				}
			}
		};
		return cunstomizer;
	}

	@Getter
	@Setter
	@ConfigurationProperties(prefix = JacksonExtSerializationProperties.PREFIX)
	public static class JacksonExtSerializationProperties {
		public static final String PREFIX = "spring.jackson.ext.serialization";

		/**
		 * 是否序列化Long类型数据为String类型
		 */
		private boolean writeLongAsString = false;

		/**
		 * 数组或集合为null时是否序列化为空字符串
		 */
		private boolean writeNullValueAsEmptyString = false;

		/**
		 * 数组或集合为null时是否序列化为空字符串
		 */
		private boolean writeNullArrayAsEmptyString = false;

		/**
		 * 字符串为null时是否序列化为空字符串
		 */
		private boolean writeNullStringAsEmptyString = false;

		/**
		 * Number为null时是否序列化为空字符串
		 */
		private boolean writeNullNumberAsEmptyString = false;

		/**
		 * Boolean为null时是否序列化为空字符串
		 */
		private boolean writeNullBooleanAsEmptyString = false;
	}
}
