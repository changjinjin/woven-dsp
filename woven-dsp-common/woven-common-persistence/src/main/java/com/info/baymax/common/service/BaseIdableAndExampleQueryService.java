package com.info.baymax.common.service;

import com.info.baymax.common.service.criteria.ExampleQueryService;
import com.info.baymax.common.mybatis.mapper.base.BaseExampleMapper;

public interface BaseIdableAndExampleQueryService<T> extends ExampleQueryService<T>, BaseIdableService<T> {

	@Override
	default BaseExampleMapper<T> getBaseExampleMapper() {
		return getMyIdableMapper();
	}

}
