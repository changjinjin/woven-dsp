package com.info.baymax.dsp.data.platform.mybatis.type;

import com.info.baymax.common.mybatis.type.clob.ClobVsMapTypeHandler;
import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.dataset.bean.TransformRule;

public class ClobVsMapStringKeyTransformRuleValueTypeHandler extends ClobVsMapTypeHandler<String, TransformRule> {

	@Override
	public String toKey(String k) {
		return k;
	}

	@Override
	public TransformRule toValue(Object v) {
		if (v == null) {
			return null;
		}
		return JsonBuilder.getInstance().fromJson(JsonBuilder.getInstance().toJson(v), TransformRule.class);
	}

}
