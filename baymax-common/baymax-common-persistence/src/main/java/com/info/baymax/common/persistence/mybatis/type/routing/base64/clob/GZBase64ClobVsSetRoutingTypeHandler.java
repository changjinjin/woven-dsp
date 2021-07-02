package com.info.baymax.common.persistence.mybatis.type.routing.base64.clob;

import com.info.baymax.common.persistence.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.persistence.mybatis.type.routing.clob.AbstractClobRoutingTypeHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public abstract class GZBase64ClobVsSetRoutingTypeHandler<T> extends AbstractClobRoutingTypeHandler<Set<T>>
	implements GZBase64Parser {

	protected Class<T> valueClass;

	@SuppressWarnings("unchecked")
	public GZBase64ClobVsSetRoutingTypeHandler() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		valueClass = (Class<T>) params[0];
	}

	@Override
	public String translate2Str(Set<T> t) {
		return encodeToJson(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<T> translate2Bean(String result) {
		return decodeFromJson(result, HashSet.class, valueClass);
	}
}
