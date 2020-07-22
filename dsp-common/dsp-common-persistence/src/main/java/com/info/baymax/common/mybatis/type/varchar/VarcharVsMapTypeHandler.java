package com.info.baymax.common.mybatis.type.varchar;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public abstract class VarcharVsMapTypeHandler<K, V> extends AbstractVarcharTypeHandler<Map<K, V>> {

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
