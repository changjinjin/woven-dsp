package com.info.baymax.dsp.data.consumer.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.consumer.entity.Customer;

@Mapper
public interface CustomerMapper extends MyIdableMapper<Customer> {
}
