package com.info.baymax.common.mybatis.type.blob;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * Blob VS Integer Array TypeHandler
 * @author jingwei.yang
 * @date 2019-05-29 09:41
 */
public class BlobVsIntegerArrayTypeHandler extends AbstractBlobTypeHandler<Integer[]> {

	@Override
	public String translate2Str(Integer[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public Integer[] translate2Bean(String result) {
		return Arrays.stream(result.split(DEFAULT_REGEX)).map(Integer::valueOf).toArray(Integer[]::new);
	}
}
