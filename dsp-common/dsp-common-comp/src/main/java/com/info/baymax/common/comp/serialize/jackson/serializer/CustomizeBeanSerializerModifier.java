package com.info.baymax.common.comp.serialize.jackson.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Collection;
import java.util.List;

public class CustomizeBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        for (Object beanProperty : beanProperties) {
            BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
            // 判断字段的类型，如果是array，list，set则注册nullSerializer
            if (isArrayType(writer)) {
                // 给writer注册一个自己的nullSerializer
                writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullArrayJsonSerializer());
            } else if (isStringType(writer)) {
                writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullStringJsonSerializer());
            } /*
             * else if (isNumberType(writer)) { writer.assignNullSerializer(new
             * CustomizeNullJsonSerializer.NullNumberJsonSerializer()); } else if (isBooleanType(writer)) {
             * writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullBooleanJsonSerializer()); }
             */ else {
                writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullStringJsonSerializer());
            }
        }
        return beanProperties;
    }

    /**
     * 是否是数组
     */
    private boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是string
     */
    private boolean isStringType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是int
     */
    private boolean isNumberType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return Number.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是boolean
     */
    private boolean isBooleanType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(Boolean.class);
    }

}
