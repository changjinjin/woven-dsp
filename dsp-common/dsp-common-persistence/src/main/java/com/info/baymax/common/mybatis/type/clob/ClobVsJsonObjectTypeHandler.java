package com.info.baymax.common.mybatis.type.clob;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Clob VS JsonObject TypeHandler
 *
 * @author jingwei.yang
 * @date 2019-05-29 09:46
 */
public class ClobVsJsonObjectTypeHandler extends AbstractClobTypeHandler<JSONObject> {

	@Override
	public String translate2Str(JSONObject t) {
		return JSON.toJSONString(t);
	}

	@Override
	public JSONObject translate2Bean(String result) {
		return JSON.parseObject(result);
	}
}
