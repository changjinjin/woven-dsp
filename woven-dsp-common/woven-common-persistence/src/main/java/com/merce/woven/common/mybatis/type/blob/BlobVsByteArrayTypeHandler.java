package com.merce.woven.common.mybatis.type.blob;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * 说明： Blob VS Byte Array TypeHandler. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年11月10日 下午12:44:32
 */
public class BlobVsByteArrayTypeHandler extends AbstractBlobTypeHandler<Byte[]> {

	@Override
	public String translate2Str(Byte[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public Byte[] translate2Bean(String result) {
		return Arrays.stream(result.split(DEFAULT_REGEX)).map(Byte::valueOf).toArray(Byte[]::new);
	}
}
