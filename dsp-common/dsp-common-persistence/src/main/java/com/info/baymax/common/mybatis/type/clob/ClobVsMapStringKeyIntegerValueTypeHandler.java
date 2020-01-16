package com.info.baymax.common.mybatis.type.clob;

public class ClobVsMapStringKeyIntegerValueTypeHandler extends ClobVsMapTypeHandler<String, Integer> {

    @Override
    public String toKey(String k) {
        return k;
    }

    @Override
    public Integer toValue(Object v) {
        if (v == null) {
            return null;
        }
        return Integer.valueOf(v.toString());
    }

}
