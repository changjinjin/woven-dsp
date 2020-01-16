package com.info.baymax.common.mybatis.type.clob;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * Clob VS JsonArray TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:46
 */
public class ClobVsJsonArrayTypeHandler extends AbstractClobTypeHandler<JSONArray> {

	@Override
	public String translate2Str(JSONArray t) {
		return JSON.toJSONString(t);
	}

	@Override
	public JSONArray translate2Bean(String result) {
		return JSON.parseArray(result);
	}
}
