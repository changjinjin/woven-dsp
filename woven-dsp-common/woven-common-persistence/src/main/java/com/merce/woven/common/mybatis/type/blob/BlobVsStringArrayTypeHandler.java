package com.merce.woven.common.mybatis.type.blob;

import org.apache.commons.lang3.StringUtils;

/**
 * Blob VS String Array TypeHandler
 * @author jingwei.yang
 * @date 2019-05-29 09:42
 */
public class BlobVsStringArrayTypeHandler extends AbstractBlobTypeHandler<String[]> {

	@Override
	public String translate2Str(String[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public String[] translate2Bean(String result) {
		return result.split(DEFAULT_REGEX);
	}
}
