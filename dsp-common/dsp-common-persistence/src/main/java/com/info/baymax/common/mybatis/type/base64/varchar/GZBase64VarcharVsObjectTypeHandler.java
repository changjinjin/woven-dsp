package com.info.baymax.common.mybatis.type.base64.varchar;

import com.info.baymax.common.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.mybatis.type.varchar.AbstractVarcharTypeHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class GZBase64VarcharVsObjectTypeHandler<T> extends AbstractVarcharTypeHandler<T>
    implements GZBase64Parser {

    protected Class<T> valueClass;

    @SuppressWarnings("unchecked")
    public GZBase64VarcharVsObjectTypeHandler() {
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
