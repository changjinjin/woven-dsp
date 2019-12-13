package com.info.baymax.common.mybatis.type.base64.varchar;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class GZBase64VarcharVsMapStringKeyMapValueTypeHandler extends GZBase64VarcharVsMapTypeHandler<String, Map<?, ?>> {

    @Override
    public String toKey(String k) {
        return k;
    }

    @Override
    public Map<?, ?> toValue(Object v) {
        return JSON.parseObject(v.toString());
    }

}
