package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import org.apache.ibatis.annotations.Mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;

@Mapper
public interface TenantMapper extends MyIdableMapper<Tenant> {
}
