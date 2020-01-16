package com.info.baymax.common.mybatis.type.base64.varchar;

public class GZBase64VarcharVsMapStringKeyIntegerValueTypeHandler extends GZBase64VarcharVsMapTypeHandler<String, Object> {

    @Override
    public String toKey(String k) {
        return k;
    }

    @Override
    public Object toValue(Object v) {
        if (v == null) {
            return null;
        }
        return v;
    }
}
