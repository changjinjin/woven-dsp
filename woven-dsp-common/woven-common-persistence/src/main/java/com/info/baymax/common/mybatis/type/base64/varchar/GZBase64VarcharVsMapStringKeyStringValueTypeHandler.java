package com.info.baymax.common.mybatis.type.base64.varchar;

public class GZBase64VarcharVsMapStringKeyStringValueTypeHandler extends GZBase64VarcharVsMapTypeHandler<String, String> {

    @Override
    public String toKey(String k) {
        return k;
    }

    @Override
    public String toValue(Object v) {
        if (v == null) {
            return null;
        }
        return v.toString();
    }

}
