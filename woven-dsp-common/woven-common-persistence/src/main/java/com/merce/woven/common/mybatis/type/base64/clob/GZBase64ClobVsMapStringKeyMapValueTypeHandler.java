package com.merce.woven.common.mybatis.type.base64.clob;

import java.util.Map;

import com.alibaba.fastjson.JSON;

public class GZBase64ClobVsMapStringKeyMapValueTypeHandler extends GZBase64ClobVsMapTypeHandler<String, Map<?, ?>> {

    @Override
    public String toKey(String k) {
        return k;
    }

    @Override
    public Map<?, ?> toValue(Object v) {
        return JSON.parseObject(v.toString());
    }

}
