package com.info.baymax.common.mybatis.type.clob;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * Clob VS Object TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:46
 */
public class ClobVsObjectTypeHandler<T> extends AbstractClobTypeHandler<T> {

	protected Class<T> valueClass;

	@SuppressWarnings("unchecked")
	public ClobVsObjectTypeHandler() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		valueClass = (Class<T>) params[0];
	}

	@Override
	public String translate2Str(T t) {
		if (t == null) {
			return null;
		}
		return JSON.toJSONString(t);
	}

	@Override
	public T translate2Bean(String result) {
		if (StringUtils.isEmpty(result)) {
			return null;
		}
		return JSON.parseObject(result, valueClass);
	}

}
