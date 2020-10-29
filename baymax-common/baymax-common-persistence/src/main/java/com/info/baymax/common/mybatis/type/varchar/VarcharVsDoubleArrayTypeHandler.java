package com.info.baymax.common.mybatis.type.varchar;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * Varchar VS Double Array TypeHandler
 * @author jingwei.yang
 * @date 2019-05-28 13:53
 */
public class VarcharVsDoubleArrayTypeHandler extends AbstractVarcharTypeHandler<Double[]> {

	@Override
	public String translate2Str(Double[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public Double[] translate2Bean(String result) {
		return Arrays.stream(result.split(DEFAULT_REGEX)).map(Double::valueOf).toArray(Double[]::new);
	}
}
