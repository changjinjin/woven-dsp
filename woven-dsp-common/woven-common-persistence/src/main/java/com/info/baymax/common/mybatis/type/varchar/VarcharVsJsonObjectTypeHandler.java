package com.info.baymax.common.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Varchar VS JsonObject TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-28 13:52
 */
public class VarcharVsJsonObjectTypeHandler extends AbstractVarcharTypeHandler<JSONObject> {

	@Override
	public String translate2Str(JSONObject t) {
		return StringUtils.join(t, DEFAULT_REGEX);
	}

	@Override
	public JSONObject translate2Bean(String result) {
		return JSON.parseObject(result);
	}
}
