package com.merce.woven.common.mybatis.type.clob;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * Clob VS Short Arrsy TypeHandler
 * @author jingwei.yang
 * @date 2019-05-29 09:47
 */
public class ClobVsShortArrayTypeHandler extends AbstractClobTypeHandler<Short[]> {

	@Override
	public String translate2Str(Short[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public Short[] translate2Bean(String result) {
		return Arrays.stream(result.split(DEFAULT_REGEX)).map(Short::valueOf).toArray(Short[]::new);
	}
}
