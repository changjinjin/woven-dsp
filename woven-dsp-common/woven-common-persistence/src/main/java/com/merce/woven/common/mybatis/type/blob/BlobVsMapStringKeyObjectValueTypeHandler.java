package com.merce.woven.common.mybatis.type.blob;


public class BlobVsMapStringKeyObjectValueTypeHandler extends BlobVsMapTypeHandler<String, Object> {

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
