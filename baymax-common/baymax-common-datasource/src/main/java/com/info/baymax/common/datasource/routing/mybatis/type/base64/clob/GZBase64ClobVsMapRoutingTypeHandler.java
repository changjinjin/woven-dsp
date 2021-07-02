package com.info.baymax.common.datasource.routing.mybatis.type.base64.clob;

import com.info.baymax.common.datasource.routing.mybatis.type.clob.AbstractClobRoutingTypeHandler;
import com.info.baymax.common.persistence.mybatis.type.base64.GZBase64Parser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class GZBase64ClobVsMapRoutingTypeHandler<K, V> extends AbstractClobRoutingTypeHandler<Map<K, V>>
    implements GZBase64Parser {

    protected Class<K> keyClass;
    protected Class<V> valueClass;

    @SuppressWarnings("unchecked")
    public GZBase64ClobVsMapRoutingTypeHandler() {
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
        return encodeToJson(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<K, V> translate2Bean(String result) {
        return decodeFromJson(result, HashMap.class, keyClass, valueClass);
    }
}
