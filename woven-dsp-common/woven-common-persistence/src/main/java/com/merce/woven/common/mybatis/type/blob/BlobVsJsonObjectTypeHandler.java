package com.merce.woven.common.mybatis.type.blob;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Blob VS JsonObject TypeHandler
 * @author jingwei.yang
 * @date 2019-05-29 09:42
 */
public class BlobVsJsonObjectTypeHandler extends AbstractBlobTypeHandler<JSONObject> {

	@Override
	public String translate2Str(JSONObject t) {
		return JSON.toJSONString(t);
	}

	@Override
	public JSONObject translate2Bean(String result) {
		return JSON.parseObject(result);
	}
}
