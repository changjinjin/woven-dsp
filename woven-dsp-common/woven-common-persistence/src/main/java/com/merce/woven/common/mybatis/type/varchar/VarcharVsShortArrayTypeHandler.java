package com.merce.woven.common.mybatis.type.varchar;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;


/**
 * Varchar VS Short Array TypeHandler
 * @author jingwei.yang
 * @date 2019-05-28 13:51
 */
public class VarcharVsShortArrayTypeHandler extends AbstractVarcharTypeHandler<Short[]> {

	@Override
	public String translate2Str(Short[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public Short[] translate2Bean(String result) {
		return Arrays.stream(result.split(DEFAULT_REGEX)).map(Short::valueOf).toArray(Short[]::new);
	}
}
