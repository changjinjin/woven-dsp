package com.info.baymax.common.mybatis.type.base64.clob;

public class GZBase64ClobVsMapStringKeyObjectValueTypeHandler extends GZBase64ClobVsMapTypeHandler<String, Object> {

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
