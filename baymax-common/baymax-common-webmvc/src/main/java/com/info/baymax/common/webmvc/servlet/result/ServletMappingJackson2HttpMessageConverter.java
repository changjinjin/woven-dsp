package com.info.baymax.common.webmvc.servlet.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.comp.serialize.jackson.serializer.CustomizeBeanSerializerModifier;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class ServletMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public ServletMappingJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = getObjectMapper();
        objectMapper.setSerializerFactory(
            objectMapper.getSerializerFactory().withSerializerModifier(new CustomizeBeanSerializerModifier()));
    }

}
