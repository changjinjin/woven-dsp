package com.merce.woven.common.mybatis.type.varchar;

public class VarcharVsMapStringKeyIntegerValueTypeHandler extends VarcharVsMapTypeHandler<String, Integer> {

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
