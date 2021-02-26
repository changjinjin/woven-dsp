package com.info.baymax.common.config.serialize.jackson.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Collection;
import java.util.List;

public class CustomizeBeanSerializerModifier extends BeanSerializerModifier {
    private final CustomizeNullJsonSerializer.NullArrayJsonSerializer nullArrayJsonSerializer = new CustomizeNullJsonSerializer.NullArrayJsonSerializer();
    private final CustomizeNullJsonSerializer.NullStringJsonSerializer nullStringJsonSerializer = new CustomizeNullJsonSerializer.NullStringJsonSerializer();
    private final CustomizeNullJsonSerializer.NullNumberJsonSerializer nullNumberJsonSerializer = new CustomizeNullJsonSerializer.NullNumberJsonSerializer();
    private final CustomizeNullJsonSerializer.NullBooleanJsonSerializer nullBooleanJsonSerializer = new CustomizeNullJsonSerializer.NullBooleanJsonSerializer();

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

    public CustomizeBeanSerializerModifier(boolean writeNullValueAsEmptyString, boolean writeNullArrayAsEmptyString,
                                           boolean writeNullStringAsEmptyString, boolean writeNullNumberAsEmptyString,
                                           boolean writeNullBooleanAsEmptyString) {
        this.writeNullValueAsEmptyString = writeNullValueAsEmptyString;
        this.writeNullArrayAsEmptyString = writeNullArrayAsEmptyString;
        this.writeNullStringAsEmptyString = writeNullStringAsEmptyString;
        this.writeNullNumberAsEmptyString = writeNullNumberAsEmptyString;
        this.writeNullBooleanAsEmptyString = writeNullBooleanAsEmptyString;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        for (Object beanProperty : beanProperties) {
            BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
            // 判断字段的类型，如果是array，list，set则注册nullSerializer
            if (writeNullArrayAsEmptyString && isArrayType(writer)) {
                writer.assignNullSerializer(nullArrayJsonSerializer);
            } else if (writeNullStringAsEmptyString && isStringType(writer)) {
                writer.assignNullSerializer(nullStringJsonSerializer);
            } else if (writeNullNumberAsEmptyString && isNumberType(writer)) {
                writer.assignNullSerializer(nullNumberJsonSerializer);
            } else if (writeNullBooleanAsEmptyString && isBooleanType(writer)) {
                writer.assignNullSerializer(nullBooleanJsonSerializer);
            } else if (writeNullValueAsEmptyString) {
                writer.assignNullSerializer(nullStringJsonSerializer);
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
