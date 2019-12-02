package com.merce.woven.dsp.access.platform.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.merce.woven.common.mybatis.mapper.MyIdableMapper;
import com.merce.woven.dsp.access.platform.entity.User;

@Mapper
public interface UserMapper extends MyIdableMapper<User> {
}
