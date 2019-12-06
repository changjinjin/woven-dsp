package com.merce.woven.common.comp.serialize.jackson.introspector;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.merce.woven.common.comp.serialize.annotation.NumberFormat;
import com.merce.woven.common.comp.serialize.jackson.serializer.NumberFormatDeserializer;
import com.merce.woven.common.comp.serialize.jackson.serializer.NumberFormatSerializer;

/**
 * 扩展Jackson注解解析内省器
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:57:01
 */
public class JacksonExtAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private static final long serialVersionUID = 7020491410174689366L;

    @Override
    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    @Override
    public Object findSerializer(Annotated a) {
        NumberFormat numberFormat = _findAnnotation(a, NumberFormat.class);
        if (numberFormat != null) {
            return new NumberFormatSerializer(numberFormat.pattern(), numberFormat.serialize());
        }
        return super.findSerializer(a);
    }

    @Override
    public Object findDeserializer(Annotated a) {
        NumberFormat numberFormat = _findAnnotation(a, NumberFormat.class);
        if (numberFormat != null) {
            return new NumberFormatDeserializer(numberFormat.pattern(), numberFormat.deserialize());
        }
        return super.findDeserializer(a);
    }

}
