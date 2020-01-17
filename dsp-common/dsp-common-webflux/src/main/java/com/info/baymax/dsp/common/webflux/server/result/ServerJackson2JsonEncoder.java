package com.info.baymax.dsp.common.webflux.server.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.comp.serialize.jackson.serializer.CustomizeBeanSerializerModifier;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

public class ServerJackson2JsonEncoder extends Jackson2JsonEncoder {
    public ServerJackson2JsonEncoder() {
        ObjectMapper objectMapper = getObjectMapper();
        objectMapper.setSerializerFactory(
            objectMapper.getSerializerFactory().withSerializerModifier(new CustomizeBeanSerializerModifier()));
    }
}
