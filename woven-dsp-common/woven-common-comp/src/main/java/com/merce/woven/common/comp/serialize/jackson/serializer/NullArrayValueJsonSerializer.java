package com.merce.woven.common.comp.serialize.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 数组空值序列化器
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午3:47:06
 */
public class NullArrayValueJsonSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
        if (value == null) {
            jgen.writeStartArray();
            jgen.writeEndArray();
        } else {
            jgen.writeObject(value);
        }
    }
}
