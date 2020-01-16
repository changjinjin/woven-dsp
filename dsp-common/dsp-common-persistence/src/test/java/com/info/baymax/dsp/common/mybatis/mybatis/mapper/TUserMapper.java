package com.info.baymax.dsp.common.mybatis.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.info.baymax.common.mybatis.mapper.extension.BaseIdableExtensionMapper;
import com.info.baymax.dsp.common.mybatis.mybatis.entity.TUser;

@Mapper
public interface TUserMapper extends BaseIdableExtensionMapper<TUser> {
}