package com.info.baymax.common.mybatis.type.varchar;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * Varchar VS Byte Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-28 13:53
 */
public class VarcharVsByteArrayTypeHandler extends AbstractVarcharTypeHandler<Byte[]> {

	@Override
	public String translate2Str(Byte[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public Byte[] translate2Bean(String result) {
		return Arrays.stream(result.split(DEFAULT_REGEX)).map(Byte::valueOf).toArray(Byte[]::new);
	}

}
