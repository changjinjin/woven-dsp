package com.info.baymax.common.persistence.service;

import com.info.baymax.common.persistence.service.criteria.example.ExampleQueryService;

public interface BaseAndExampleQueryService<T> extends ExampleQueryService<T>, BaseService<T> {
}
