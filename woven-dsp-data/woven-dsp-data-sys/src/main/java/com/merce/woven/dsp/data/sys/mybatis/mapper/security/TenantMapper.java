package com.merce.woven.dsp.data.sys.mybatis.mapper.security;

import org.apache.ibatis.annotations.Mapper;

import com.merce.woven.common.mybatis.mapper.MyIdableMapper;
import com.merce.woven.dsp.data.sys.entity.security.Tenant;

@Mapper
public interface TenantMapper extends MyIdableMapper<Tenant> {
}
