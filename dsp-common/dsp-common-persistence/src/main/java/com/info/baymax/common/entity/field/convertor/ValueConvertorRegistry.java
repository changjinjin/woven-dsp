package com.info.baymax.common.entity.field.convertor;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 默认值处理器注册，单例实现
 *
 * @author jingwei.yang
 * @date 2019年10月10日 上午9:57:51
 */
public final class ValueConvertorRegistry {

    /**
     * 缓存注册过的转换器实例
     */
    private final Map<Class<?>, ValueConvertor<?>> defauleValueConvertorMap = new HashMap<>();

    /**
     * 单实例的ValueConvertorRegistry对象
     */
    private static ValueConvertorRegistry singleton = null;

    /**
     * 双检锁实现单利模式，避免频繁创建对象的消耗和线程安全问题
     */
    public static ValueConvertorRegistry getInstance() {
        if (singleton == null) {
            synchronized (ValueConvertorRegistry.class) {
                if (singleton == null) {
                    singleton = new ValueConvertorRegistry();
                }
            }
        }
        return singleton;
    }

    // 只允许内部调用
    private ValueConvertorRegistry() {
        register(Boolean.class, new BooleanValueConvertor());
        register(boolean.class, new BooleanValueConvertor());

        register(Byte.class, new ByteValueConvertor());
        register(byte.class, new ByteValueConvertor());

        register(Short.class, new ShortValueConvertor());
        register(short.class, new ShortValueConvertor());

        register(Integer.class, new IntegerValueConvertor());
        register(int.class, new IntegerValueConvertor());

        register(Long.class, new LongValueConvertor());
        register(long.class, new LongValueConvertor());

        register(Float.class, new FloatValueConvertor());
        register(float.class, new FloatValueConvertor());

        register(Double.class, new DoubleValueConvertor());
        register(double.class, new DoubleValueConvertor());

        register(Character.class, new CharacterValueConvertor());
        register(char.class, new CharacterValueConvertor());

        register(String.class, new StringValueConvertor());

        register(Date.class, new DateValueConvertor());
    }

    public boolean hasValueConvertor(Class<?> javaType) {
        return javaType != null && getValueConvertor(javaType) != null;
    }

    public ValueConvertor<?> getValueConvertor(Class<?> javaType) {
        ValueConvertor<?> valueConvertor = defauleValueConvertorMap.get(javaType);
        if (valueConvertor != null) {
            return valueConvertor;
        }
        return new UnknownValueConvertor();
    }

    private void register(Class<?> clazz, ValueConvertor<?> valueConvertor) {
        MappedTypes mappedValueTypes = valueConvertor.getClass().getAnnotation(MappedTypes.class);
        if (mappedValueTypes != null) {
            for (Class<?> javaType : mappedValueTypes.value()) {
                defauleValueConvertorMap.put(javaType, valueConvertor);
            }
        } else {
            defauleValueConvertorMap.put(clazz, valueConvertor);
        }
    }

    public <T> void register(ValueConvertor<T> valueConvertor) {
        MappedTypes mappedTypes = valueConvertor.getClass().getAnnotation(MappedTypes.class);
        if (mappedTypes != null) {
            for (Class<?> handledType : mappedTypes.value()) {
                register(handledType, valueConvertor);
            }
        }
    }

    public void register(Class<?> valueConvertorClass) {
        boolean mappedTypeFound = false;
        MappedTypes mappedTypes = valueConvertorClass.getAnnotation(MappedTypes.class);
        if (mappedTypes != null) {
            for (Class<?> javaTypeClass : mappedTypes.value()) {
                register(javaTypeClass, valueConvertorClass);
                mappedTypeFound = true;
            }
        }
        if (!mappedTypeFound) {
            register(getConvertor(null, valueConvertorClass));
        }
    }

    public void register(String javaTypeClassName, String valueConvertorClassName) throws ClassNotFoundException {
        register(Resources.classForName(javaTypeClassName), Resources.classForName(valueConvertorClassName));
    }

    public void register(Class<?> javaTypeClass, Class<?> convertorClass) {
        register(javaTypeClass, getConvertor(javaTypeClass, convertorClass));
    }

    @SuppressWarnings("unchecked")
    public <T> ValueConvertor<T> getConvertor(Class<?> javaTypeClass, Class<?> convertorClass) {
        if (javaTypeClass != null) {
            try {
                Constructor<?> c = convertorClass.getConstructor(Class.class);
                return (ValueConvertor<T>) c.newInstance(javaTypeClass);
            } catch (NoSuchMethodException ignored) {
                // ignored
            } catch (Exception e) {
                throw new TypeException("Failed invoking constructor for handler " + convertorClass, e);
            }
        }
        try {
            Constructor<?> c = convertorClass.getConstructor();
            return (ValueConvertor<T>) c.newInstance();
        } catch (Exception e) {
            throw new TypeException("Unable to find a usable constructor for " + convertorClass, e);
        }
    }

    // scan
    public void register(String packageName) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.find(new ResolverUtil.IsA(ValueConvertor.class), packageName);
        Set<Class<? extends Class<?>>> handlerSet = resolverUtil.getClasses();
        for (Class<?> type : handlerSet) {
            // Ignore inner classes and interfaces (including package-info.java)
            // and abstract classes
            if (!type.isAnonymousClass() && !type.isInterface() && !Modifier.isAbstract(type.getModifiers())) {
                register(type);
            }
        }
    }

    public Collection<ValueConvertor<?>> getTypeHandlers() {
        return Collections.unmodifiableCollection(defauleValueConvertorMap.values());
    }

}
