package com.info.baymax.common.mybatis.type.clob;

import org.apache.commons.lang3.StringUtils;

/**
 * Clob VS String Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:47
 */
public class ClobVsStringArrayTypeHandler extends AbstractClobTypeHandler<String[]> {

	@Override
	public String translate2Str(String[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public String[] translate2Bean(String result) {
		return result.split(DEFAULT_REGEX);
	}
}
