package com.info.baymax.common.mybatis.type.varchar;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class VarcharVsMapTypeHandler<K, V> extends AbstractVarcharTypeHandler<Map<K, V>> {

    protected Class<K> keyClass;
    protected Class<V> valueClass;

    @SuppressWarnings("unchecked")
    public VarcharVsMapTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        keyClass = (Class<K>) params[0];
        Type type = params[1];
        if (type instanceof Class) {
            valueClass = (Class<V>) type;
        } else if (type instanceof ParameterizedType) {
            valueClass = (Class<V>) ((ParameterizedType) type).getRawType();
        }
    }

    @Override
    public String translate2Str(Map<K, V> t) {
        return toJson(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<K, V> translate2Bean(String result) {
        return fromJson(result, HashMap.class, keyClass, valueClass);
    }
}
