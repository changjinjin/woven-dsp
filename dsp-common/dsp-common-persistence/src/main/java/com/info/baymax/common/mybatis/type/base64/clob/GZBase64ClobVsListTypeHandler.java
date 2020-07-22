package com.info.baymax.common.mybatis.type.base64.clob;

import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.mybatis.type.clob.AbstractClobTypeHandler;

import java.util.List;

public abstract class GZBase64ClobVsListTypeHandler<T> extends AbstractClobTypeHandler<List<T>>
    implements GZBase64Parser {

    @Override
    public String translate2Str(List<T> t) {
        return encodeToJson(t);
    }

    @Override
    public List<T> translate2Bean(String result) {
        return decodeFromJson(result, new TypeReference<List<T>>() {
        });
    }
}
