package com.merce.woven.common.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

/**
 * Varchar VS String Array TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-28 13:51
 */
public class VarcharVsStringArrayTypeHandler extends AbstractVarcharTypeHandler<String[]> {

	@Override
	public String translate2Str(String[] t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public String[] translate2Bean(String result) {
		return result.split(DEFAULT_REGEX);
	}
}
