package com.info.baymax.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.info.baymax.common.config.JacksonConfig.JacksonExtSerializationProperties;
import com.info.baymax.common.config.serialize.jackson.serializer.CustomizeBeanSerializerModifier;
import com.info.baymax.common.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Configuration
@EnableConfigurationProperties(JacksonExtSerializationProperties.class)
public class JacksonConfig {

	@Bean
	@Primary
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder,
									 JacksonExtSerializationProperties extProperties) {
		log.debug("extProperties:" + JsonUtils.toJson(extProperties, true));
		return config(builder.build(), extProperties);
	}

	public static ObjectMapper config() {
		return config(null, null);
	}

	// @Bean
	// public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(
	// JacksonExtSerializationProperties extProperties) {
	// Jackson2ObjectMapperBuilderCustomizer cunstomizer = new Jackson2ObjectMapperBuilderCustomizer() {
	// @Override
	// public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
	// jacksonObjectMapperBuilder.failOnUnknownProperties(false);
	// jacksonObjectMapperBuilder.createXmlMapper(false);
	// jacksonObjectMapperBuilder.featuresToDisable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
	// jacksonObjectMapperBuilder.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	//
	// if (extProperties != null && extProperties.isWriteLongAsString()) {
	// jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
	// jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
	// jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
	// jacksonObjectMapperBuilder.serializerByType(BigDecimal.class, ToStringSerializer.instance);
	// }
	// }
	// };
	// return cunstomizer;
	// }

	public static ObjectMapper config(ObjectMapper m, JacksonExtSerializationProperties extProperties) {
		if (m == null) {
			m = Jackson2ObjectMapperBuilder.json().build();
		}
		m.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		m.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		// m.setSerializationInclusion(Include.NON_NULL);

		if (extProperties != null && extProperties.isWriteLongAsString()) {
			SimpleModule simpleModule = new SimpleModule();
			simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
			simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
			simpleModule.addSerializer(long.class, ToStringSerializer.instance);
			simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
			simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
			m.registerModule(simpleModule);
		}

		/** 为objectMapper注册一个带有SerializerModifier的Factory */
		m.setSerializerFactory(m.getSerializerFactory()
			.withSerializerModifier(new CustomizeBeanSerializerModifier(
				extProperties.isWriteNullValueAsEmptyString(), extProperties.isWriteNullArrayAsEmptyString(),
				extProperties.isWriteNullStringAsEmptyString(), extProperties.isWriteNullNumberAsEmptyString(),
				extProperties.isWriteNullBooleanAsEmptyString())));

		/** Object **/
		// SerializerProvider serializerProvider = m.getSerializerProvider();
		// serializerProvider.setNullValueSerializer(new CustomizeNullJsonSerializer.NullObjectJsonSerializer());
		return m;
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
