package com.info.baymax.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.info.baymax.common.config.JacksonConfig.JacksonExtProperties;
import com.info.baymax.common.config.serialize.jackson.serializer.CustomizeBeanSerializerModifier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
@EnableConfigurationProperties(JacksonExtProperties.class)
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder, JacksonExtProperties extProperties) {
        return config(builder.createXmlMapper(false).build(), extProperties);
    }

    public static ObjectMapper config() {
        return config(null, null);
    }

    public static ObjectMapper config(ObjectMapper m, JacksonExtProperties extProperties) {
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
            m.registerModule(simpleModule);
        }

        /** 为objectMapper注册一个带有SerializerModifier的Factory */
        m.setSerializerFactory(m.getSerializerFactory().withSerializerModifier(new CustomizeBeanSerializerModifier()));

        /** Object **/
        // SerializerProvider serializerProvider = m.getSerializerProvider();
        // serializerProvider.setNullValueSerializer(new CustomizeNullJsonSerializer.NullObjectJsonSerializer());
        return m;
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = JacksonExtProperties.PREFIX)
    public static class JacksonExtProperties {
        public static final String PREFIX = "spring.jackson.serialization";

        /**
         * 是否序列化Long类型数据为String类型
         */
        private boolean writeLongAsString = false;
    }
}
