package com.info.baymax.common.mybatis.type.clob;

public class ClobVsMapStringKeyObjectValueTypeHandler extends ClobVsMapTypeHandler<String, Object> {

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
