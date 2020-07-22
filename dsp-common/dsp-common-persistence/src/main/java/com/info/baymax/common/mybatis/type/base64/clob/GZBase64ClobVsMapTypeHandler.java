package com.info.baymax.common.mybatis.type.base64.clob;

import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.mybatis.type.clob.AbstractClobTypeHandler;

import java.util.Map;

public abstract class GZBase64ClobVsMapTypeHandler<K, V> extends AbstractClobTypeHandler<Map<K, V>>
    implements GZBase64Parser {

    @Override
    public String translate2Str(Map<K, V> t) {
        return encodeToJson(t);
    }

    @Override
    public Map<K, V> translate2Bean(String result) {
        return decodeFromJson(result, new TypeReference<Map<K, V>>() {
        });
    }
}
