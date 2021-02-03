package com.info.baymax.common.persistence.mybatis.mapper.base;

import com.info.baymax.common.persistence.mybatis.mapper.aggregation.AggregationMapper;
import com.info.baymax.common.persistence.mybatis.mapper.example.ExampleMapper;
import com.info.baymax.common.persistence.mybatis.mapper.example.SelectByExampleRowBoundsMapper;

public interface BaseExampleMapper<T>
    extends SelectByExampleRowBoundsMapper<T>, ExampleMapper<T>, AggregationMapper<T> {
}
