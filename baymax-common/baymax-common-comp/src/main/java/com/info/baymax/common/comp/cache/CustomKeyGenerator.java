package com.info.baymax.common.comp.cache;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * 说明：自定义cache key生成策略. <br>
 *
 * @author jingwei.yang
 * @date 2017年10月13日 下午2:19:43
 */
public class CustomKeyGenerator implements KeyGenerator {

	private static final Logger log = LoggerFactory.getLogger(CustomKeyGenerator.class);

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuffer sb = new StringBuffer();
		// 添加class名称
		sb.append(target.getClass().getName() + ".");
		// 添加方法名称
		sb.append(method.getName() + ".");
		// 添加参数
		if (params != null && params.length > 0) {
			for (Object obj : params) {
				if (obj != null) {
					if (obj instanceof AtomicInteger || obj instanceof AtomicLong || obj instanceof BigDecimal || obj instanceof BigInteger || obj instanceof Byte
							|| obj instanceof Double || obj instanceof Float || obj instanceof Integer || obj instanceof Long || obj instanceof Short) {
						sb.append("_" + obj);
					} else if (obj instanceof List || obj instanceof Set || obj instanceof Map) {
						sb.append("_" + obj);
					} else {
						sb.append("_" + obj.hashCode());
					}
				}
			}
		}
		int keyGenerator = sb.toString().hashCode();
		log.debug(sb.toString() + ":" + keyGenerator);
		return keyGenerator;
	}
}
