package com.merce.woven.common.mybatis.mapper.base;

import com.merce.woven.common.mybatis.mapper.example.ExampleMapper;
import com.merce.woven.common.mybatis.mapper.example.SelectByExampleRowBoundsMapper;

public interface BaseExampleMapper<T> extends SelectByExampleRowBoundsMapper<T>, ExampleMapper<T> {
}
