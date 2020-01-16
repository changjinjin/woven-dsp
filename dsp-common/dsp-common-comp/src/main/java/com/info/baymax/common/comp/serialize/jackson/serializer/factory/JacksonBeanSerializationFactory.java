package com.info.baymax.common.comp.serialize.jackson.serializer.factory;

import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.info.baymax.common.comp.serialize.jackson.modifier.NullValueBeanSerializerModifier;


/**
 * 自定义序列化工厂
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午3:46:49
 */
public class JacksonBeanSerializationFactory extends BeanSerializerFactory {
    private static final long serialVersionUID = 1L;

    private NullValueBeanSerializerModifier _nullValueBeanSerializerModifier = new NullValueBeanSerializerModifier();

    public JacksonBeanSerializationFactory() {
        this(null);
        getFactoryConfig().withSerializerModifier(_nullValueBeanSerializerModifier);
    }

    protected JacksonBeanSerializationFactory(SerializerFactoryConfig config) {
        super(config);
        config.withSerializerModifier(_nullValueBeanSerializerModifier);
    }
}
