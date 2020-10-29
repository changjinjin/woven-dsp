package com.info.baymax.common.mybatis.mapper.base;

import com.info.baymax.common.mybatis.mapper.aggregation.AggregationMapper;
import com.info.baymax.common.mybatis.mapper.example.ExampleMapper;
import com.info.baymax.common.mybatis.mapper.example.SelectByExampleRowBoundsMapper;

public interface BaseExampleMapper<T>
    extends SelectByExampleRowBoundsMapper<T>, ExampleMapper<T>, AggregationMapper<T> {
}
