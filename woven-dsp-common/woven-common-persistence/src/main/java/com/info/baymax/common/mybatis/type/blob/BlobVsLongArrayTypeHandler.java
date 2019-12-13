package com.info.baymax.common.mybatis.type.blob;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * Blob VS Long Array TypeHandler
 * @author jingwei.yang
 * @date 2019-05-29 09:42
 */
public class BlobVsLongArrayTypeHandler extends AbstractBlobTypeHandler<Long[]> {

	@Override
	public String translate2Str(Long[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public Long[] translate2Bean(String result) {
		return Arrays.stream(result.split(DEFAULT_REGEX)).map(Long::valueOf).toArray(Long[]::new);
	}
}
