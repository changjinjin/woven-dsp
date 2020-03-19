package com.info.baymax.common.entity.field.resolver;

import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.entity.field.convertor.UnknownValueConvertor;
import com.info.baymax.common.entity.field.convertor.ValueConvertor;
import com.info.baymax.common.entity.field.convertor.ValueConvertorRegistry;
import com.info.baymax.common.utils.ICollections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的属性值初始化处理器
 *
 * @author jingwei.yang
 * @date 2020年3月19日 上午9:58:48
 */
public class DefaultFieldResolver implements FieldResolver {

    /**
     * 缓存已经解析的实体类信息，减少消耗
     */
    private static final Map<Class<? extends Object>, List<EntityField>> entityFieldsCache = new HashMap<>();

    /**
     * 单实例的FieldResolver对象
     */
    private static FieldResolver singleton = null;

    /**
     * 双检锁实现单利模式，避免频繁创建对象的消耗和线程安全问题
     */
    public static FieldResolver getInstance() {
        if (singleton == null) {
            synchronized (DefaultFieldResolver.class) {
                if (singleton == null) {
                    singleton = new DefaultFieldResolver();
                }
            }
        }
        return singleton;
    }

    // 只允许内部调用
    private DefaultFieldResolver() {
    }

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
            ValueConvertor<?> valueConvertor = null;
            ValueConvertorRegistry registry = ValueConvertorRegistry.getInstance();

            String defValue = defaultValue.value();
            Class<? extends ValueConvertor<?>> convertorClass = defaultValue.convertor();
            if (convertorClass == null || convertorClass.isAssignableFrom(UnknownValueConvertor.class)) {
                valueConvertor = registry.getValueConvertor(field.getJavaType());
            } else {
                valueConvertor = registry.getConvertor(null, convertorClass);
            }
            field.setValue(object, valueConvertor.convert(defValue));
        }
    }

}
