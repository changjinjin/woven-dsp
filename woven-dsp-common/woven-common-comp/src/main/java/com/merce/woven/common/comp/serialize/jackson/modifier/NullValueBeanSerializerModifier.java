package com.merce.woven.common.comp.serialize.jackson.modifier;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.merce.woven.common.comp.serialize.jackson.serializer.NullArrayValueJsonSerializer;
import com.merce.woven.common.comp.serialize.jackson.serializer.NullValueJsonSerializer;

import java.util.List;


/**
 * 数组对象为空时的序列化修饰器，将空的数组对象序列化值转为空数组的序列化值
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午3:45:49
 */
public class NullValueBeanSerializerModifier extends BeanSerializerModifier {

    private JsonSerializer<Object> _nullArrayValueJsonSerializer = new NullArrayValueJsonSerializer();
    private JsonSerializer<Object> _nullValueJsonSerializer = new NullValueJsonSerializer();

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);
            // 判断字段的类型，如果是array，list，set则注册nullSerializer
            if (isArrayType(writer)) {
                // 给writer注册一个自己的nullSerializer
                writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
            } else {
                writer.assignNullSerializer(this.defaultNullValueJsonSerializer());
            }
        }
        return beanProperties;
    }

    /**
     * 判断需要序列化的类型是否是数组或者迭代对象
     */
    protected boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.isArray() || clazz.isAssignableFrom(Iterable.class);
    }

    protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
        return _nullArrayValueJsonSerializer;
    }

    protected JsonSerializer<Object> defaultNullValueJsonSerializer() {
        return _nullValueJsonSerializer;
    }
}
