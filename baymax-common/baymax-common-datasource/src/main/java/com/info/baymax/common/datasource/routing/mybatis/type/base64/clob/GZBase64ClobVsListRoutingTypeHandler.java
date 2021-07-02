package com.info.baymax.common.datasource.routing.mybatis.type.base64.clob;

import com.info.baymax.common.datasource.routing.mybatis.type.clob.AbstractClobRoutingTypeHandler;
import com.info.baymax.common.persistence.mybatis.type.base64.GZBase64Parser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class GZBase64ClobVsListRoutingTypeHandler<T> extends AbstractClobRoutingTypeHandler<List<T>>
    implements GZBase64Parser {

    protected Class<T> valueClass;

    @SuppressWarnings("unchecked")
    public GZBase64ClobVsListRoutingTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        valueClass = (Class<T>) params[0];
    }

    @Override
    public String translate2Str(List<T> t) {
        return encodeToJson(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> translate2Bean(String result) {
        return decodeFromJson(result, ArrayList.class, valueClass);
    }
}
