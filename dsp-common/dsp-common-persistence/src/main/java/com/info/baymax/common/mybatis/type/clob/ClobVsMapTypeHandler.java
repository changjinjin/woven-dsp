package com.info.baymax.common.mybatis.type.clob;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

public abstract class ClobVsMapTypeHandler<K, V> extends AbstractClobTypeHandler<Map<K, V>> {

    @Override
    public String translate2Str(Map<K, V> t) {
        return toJson(t);
    }

    @Override
    public Map<K, V> translate2Bean(String result) {
        return fromJson(result, new TypeReference<Map<K, V>>() {
        });
    }
}
