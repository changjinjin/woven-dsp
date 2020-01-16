package com.info.baymax.common.mybatis.type.base64.varchar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.info.baymax.common.mybatis.type.TypeHandleException;
import com.info.baymax.common.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.mybatis.type.varchar.AbstractVarcharTypeHandler;
import org.apache.commons.lang3.StringUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "restriction" })
public abstract class GZBase64VarcharVsMapTypeHandler<K, V> extends AbstractVarcharTypeHandler<Map<K, V>>
		implements GZBase64Parser {

	protected Class<K> keyClass;
	protected Class<V> valueClass;

	public GZBase64VarcharVsMapTypeHandler() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		Type type = params[0];
		if (type instanceof ParameterizedTypeImpl) {
			keyClass = (Class<K>) ((ParameterizedTypeImpl) type).getRawType();
		} else {
			keyClass = (Class<K>) type;
		}

		Type type1 = params[1];
		if (type1 instanceof ParameterizedTypeImpl) {
			valueClass = (Class<V>) ((ParameterizedTypeImpl) type1).getRawType();
		} else {
			valueClass = (Class<V>) type1;
		}
	}

	@Override
	public String translate2Str(Map<K, V> t) {
		try {
			return encode(JSON.toJSONString(t));
		} catch (IOException e) {
			e.printStackTrace();
			throw new TypeHandleException(e);
		}
	}

	@Override
	public Map<K, V> translate2Bean(String result) {
		Map<K, V> map = new HashMap<K, V>();
		JSONObject obj = null;
		try {
			String decode = decode(result);
			if (StringUtils.isEmpty(decode)) {
				return null;
			}

			obj = JSON.parseObject(decode);
			obj.forEach((k, v) -> {
				if (v != null) {
					map.put(toKey(k), toValue(v));
				} else {
					map.put(toKey(k), null);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			throw new TypeHandleException(e);
		}
		return map;
	}

	public abstract K toKey(String k);

	public abstract V toValue(Object v);

}
