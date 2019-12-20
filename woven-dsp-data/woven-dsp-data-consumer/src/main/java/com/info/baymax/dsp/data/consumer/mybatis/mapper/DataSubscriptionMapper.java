package com.info.baymax.dsp.data.consumer.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.consumer.entity.DataSubscription;

@Mapper
public interface DataSubscriptionMapper extends MyIdableMapper<DataSubscription> {
}
