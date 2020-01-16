package com.info.baymax.common.mybatis.type.base64.clob;

public class GZBase64ClobVsMapStringKeyStringValueTypeHandler extends GZBase64ClobVsMapTypeHandler<String, String> {

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
