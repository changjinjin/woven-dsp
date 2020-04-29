package com.info.baymax.common.comp.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.info.baymax.common.comp.serialize.jackson.serializer.CustomizeBeanSerializerModifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    // @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return config(builder.createXmlMapper(false).build());
    }

    public static ObjectMapper config(ObjectMapper m) {
        if (m == null) {
            m = Jackson2ObjectMapperBuilder.json().build();
        }
        m.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // m.setSerializationInclusion(Include.NON_NULL);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        m.registerModule(simpleModule);

        /** 为objectMapper注册一个带有SerializerModifier的Factory */
        m.setSerializerFactory(m.getSerializerFactory().withSerializerModifier(new CustomizeBeanSerializerModifier()));

        /** Object **/
        // SerializerProvider serializerProvider = m.getSerializerProvider();
        // serializerProvider.setNullValueSerializer(new CustomizeNullJsonSerializer.NullObjectJsonSerializer());
        return m;
    }
}
