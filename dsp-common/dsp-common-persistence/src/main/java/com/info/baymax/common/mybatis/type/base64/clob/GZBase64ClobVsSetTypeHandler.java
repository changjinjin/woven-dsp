package com.info.baymax.common.mybatis.type.base64.clob;

import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.mybatis.type.clob.AbstractClobTypeHandler;

import java.util.Set;

public abstract class GZBase64ClobVsSetTypeHandler<T> extends AbstractClobTypeHandler<Set<T>>
    implements GZBase64Parser {

    @Override
    public String translate2Str(Set<T> t) {
        return encodeToJson(t);
    }

    @Override
    public Set<T> translate2Bean(String result) {
        return decodeFromJson(result, new TypeReference<Set<T>>() {
        });
    }
}
