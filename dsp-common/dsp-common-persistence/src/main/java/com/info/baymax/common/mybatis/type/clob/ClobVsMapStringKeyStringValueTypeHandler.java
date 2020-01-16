package com.info.baymax.common.mybatis.type.clob;

public class ClobVsMapStringKeyStringValueTypeHandler extends ClobVsMapTypeHandler<String, String> {

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
