package com.info.baymax.common.persistence.mybatis.type.base64.clob;

import com.info.baymax.common.persistence.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.persistence.mybatis.type.clob.AbstractClobTypeHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class GZBase64ClobVsObjectTypeHandler<T> extends AbstractClobTypeHandler<T> implements GZBase64Parser {
    protected Class<T> valueClass;

    @SuppressWarnings("unchecked")
    public GZBase64ClobVsObjectTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        valueClass = (Class<T>) params[0];
    }

    @Override
    public String translate2Str(T t) {
        return encodeToJson(t);
    }

    @Override
    public T translate2Bean(String result) {
        return decodeFromJson(result, valueClass);
    }
}
