package com.merce.woven.common.mybatis.type.blob;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * Blob VS JsonArray TypeHandler
 * @author jingwei.yang
 * @date 2019-05-29 09:42
 */
public class BlobVsJsonArrayTypeHandler extends AbstractBlobTypeHandler<JSONArray> {

	@Override
	public String translate2Str(JSONArray t) {
		return JSON.toJSONString(t);
	}

	@Override
	public JSONArray translate2Bean(String result) {
		return JSON.parseArray(result);
	}
}
