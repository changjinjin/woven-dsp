package com.info.baymax.common.entity.field.resolver;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.entity.field.convertor.UnknownValueConvertor;
import com.info.baymax.common.entity.field.convertor.ValueConvertor;
import com.info.baymax.common.entity.field.convertor.ValueConvertorRegistry;
import com.info.baymax.common.utils.ICollections;

public class DefaultFieldResolver implements FieldResolver {

    private static final Map<Class<? extends Object>, List<EntityField>> entityFieldsCache = new HashMap<>();

    @Override
    public void resolve(Object obj) {
        Class<? extends Object> entityClass = obj.getClass();

        // 先从缓存中取，如果没有在解析
        List<EntityField> fields = entityFieldsCache.get(entityClass);
        if (ICollections.hasNoElements(fields)) {
            fields = FieldHelper.getFields(entityClass);
            entityFieldsCache.put(entityClass, fields);
        }

        if (ICollections.hasNoElements(fields)) {
            return;
        }

        for (EntityField field : fields) {
            if (!field.isAnnotationPresent(DefaultValue.class)) {
                continue;
            }
            try {
                processField(obj, field);
            } catch (Exception e) {
                throw new FieldResolveException(e.getMessage(), e);
            }
        }
    }

    // 处理field
    private void processField(Object object, EntityField field)
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (field.isAnnotationPresent(DefaultValue.class)) {
            processDefaultValue(object, field, field.getAnnotation(DefaultValue.class));
        }
    }

    // 设置field默认值
    private void processDefaultValue(Object object, EntityField field, DefaultValue defaultValue)
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object value = field.getValue(object);
        if (value == null) {
            String defValue = defaultValue.value();

            ValueConvertor<?> valueConvertor = null;
            ValueConvertorRegistry registry = new ValueConvertorRegistry();

            Class<? extends ValueConvertor<?>> convertorClass = defaultValue.convertor();
            if (convertorClass == null || convertorClass.isAssignableFrom(UnknownValueConvertor.class)) {
                valueConvertor = registry.getValueConvertor(field.getJavaType());
            } else {
                valueConvertor = registry.getInstance(null, convertorClass);
            }
            field.setValue(object, valueConvertor.convert(defValue));
        }
    }

}
