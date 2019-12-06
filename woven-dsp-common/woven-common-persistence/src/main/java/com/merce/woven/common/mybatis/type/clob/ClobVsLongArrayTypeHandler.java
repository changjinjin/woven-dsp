package com.merce.woven.common.mybatis.type.clob;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * Clob VS Long Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:46
 */
public class ClobVsLongArrayTypeHandler extends AbstractClobTypeHandler<Long[]> {

	@Override
	public String translate2Str(Long[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public Long[] translate2Bean(String result) {
		return Arrays.stream(result.split(DEFAULT_REGEX)).map(Long::valueOf).toArray(Long[]::new);
	}
}
