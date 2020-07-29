package com.info.baymax.common.mybatis.type.base64.clob;

import com.info.baymax.common.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.mybatis.type.clob.AbstractClobTypeHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public abstract class GZBase64ClobVsSetTypeHandler<T> extends AbstractClobTypeHandler<Set<T>>
    implements GZBase64Parser {

    protected Class<T> valueClass;

    @SuppressWarnings("unchecked")
    public GZBase64ClobVsSetTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        valueClass = (Class<T>) params[0];
    }

    @Override
    public String translate2Str(Set<T> t) {
        return encodeToJson(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<T> translate2Bean(String result) {
        return decodeFromJson(result, HashSet.class, valueClass);
    }
}
