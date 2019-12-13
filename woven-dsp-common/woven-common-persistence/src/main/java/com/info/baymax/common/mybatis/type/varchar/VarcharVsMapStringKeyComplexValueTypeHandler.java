package com.info.baymax.common.mybatis.type.varchar;

import com.alibaba.fastjson.JSON;

public abstract class VarcharVsMapStringKeyComplexValueTypeHandler<V> extends VarcharVsMapTypeHandler<String, V> {

	@Override
	public String toKey(String k) {
		return k;
	}

	@Override
	public V toValue(Object v) {
		if (v == null) {
			return null;
		}
		return JSON.parseObject(v.toString(), valueClass);
	}

}
