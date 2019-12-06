package com.merce.woven.common.comp.serialize.jackson.converter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.merce.woven.common.comp.serialize.jackson.javassist.ThreadJacksonMixInHolder;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

public class Jackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    private ObjectMapper objectMapper = new ObjectMapper();
    private boolean prefixJson = false;

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // super.writeInternal(object, outputMessage);
        // 判断是否需要重写objectMapper
        ObjectMapper objectMapper = this.objectMapper;// 本地化ObjectMapper，防止方法级别的ObjectMapper改变全局ObjectMapper
        if (ThreadJacksonMixInHolder.isContainsMixIn()) {
            objectMapper = ThreadJacksonMixInHolder.builderObjectMapper();
        }

        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);

        // A workaround for JsonGenerators not applying serialization features
        // https://github.com/FasterXML/jackson-databind/issues/12
        if (objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
            jsonGenerator.useDefaultPrettyPrinter();
        }

        try {
            if (this.prefixJson) {
                jsonGenerator.writeRaw("{} && ");
            }
            objectMapper.writeValue(jsonGenerator, object);
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    public boolean isPrefixJson() {
        return prefixJson;
    }

    public void setPrefixJson(boolean prefixJson) {
        this.prefixJson = prefixJson;
    }

}
