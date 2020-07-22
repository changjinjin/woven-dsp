package com.info.baymax.common.mybatis.type.base64.varchar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.mybatis.type.varchar.AbstractVarcharTypeHandler;

public abstract class GZBase64VarcharVsObjectTypeHandler<T> extends AbstractVarcharTypeHandler<T>
    implements GZBase64Parser {

    @Override
    public String translate2Str(T t) {
        return encodeToJson(t);
    }

    @Override
    public T translate2Bean(String result) {
        return decodeFromJson(result, new TypeReference<T>() {
        });
    }
}
