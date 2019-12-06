package com.merce.woven.common.base.service;

import com.merce.woven.common.base.service.criteria.ExampleQueryService;
import com.merce.woven.common.mybatis.mapper.base.BaseExampleMapper;

public interface BaseIdableAndExampleQueryService<T> extends ExampleQueryService<T>, BaseIdableService<T> {

	@Override
	default BaseExampleMapper<T> getBaseExampleMapper() {
		return getMyIdableMapper();
	}

}
