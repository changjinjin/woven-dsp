package com.merce.woven.common.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * Varchar VS JsonArray TypeHandler
 * @author jingwei.yang
 * @date 2019-05-28 13:52
 */
public class VarcharVsJsonArrayTypeHandler extends AbstractVarcharTypeHandler<JSONArray> {

	@Override
	public String translate2Str(JSONArray t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public JSONArray translate2Bean(String result) {
		return JSON.parseArray(result);
	}
}
