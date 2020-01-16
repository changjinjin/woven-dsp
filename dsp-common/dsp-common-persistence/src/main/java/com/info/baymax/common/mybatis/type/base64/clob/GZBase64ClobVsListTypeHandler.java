package com.info.baymax.common.mybatis.type.base64.clob;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.info.baymax.common.mybatis.type.TypeHandleException;
import com.info.baymax.common.mybatis.type.base64.GZBase64Parser;
import com.info.baymax.common.mybatis.type.clob.AbstractClobTypeHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class GZBase64ClobVsListTypeHandler<T> extends AbstractClobTypeHandler<List<T>>
		implements GZBase64Parser {

	protected Class<T> valueClass;

	@SuppressWarnings("unchecked")
	public GZBase64ClobVsListTypeHandler() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		valueClass = (Class<T>) params[0];
	}

	@Override
	public String translate2Str(List<T> t) {
		try {
			return encode(JSON.toJSONString(t));
		} catch (IOException e) {
			e.printStackTrace();
			throw new TypeHandleException(e);
		}
	}

	@Override
	public List<T> translate2Bean(String result) {
		try {
			String decode = decode(result);
			if (StringUtils.isEmpty(decode)) {
				return null;
			}
			return JSONArray.parseArray(decode, valueClass);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TypeHandleException(e);
		}
	}
}
