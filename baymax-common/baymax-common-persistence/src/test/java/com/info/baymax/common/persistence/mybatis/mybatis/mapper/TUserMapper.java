package com.info.baymax.common.persistence.mybatis.mybatis.mapper;

import com.info.baymax.common.persistence.mybatis.mybatis.entity.TUser;
import com.info.baymax.common.persistence.service.BaseIdableAndExampleQueryService;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TUserMapper extends BaseIdableAndExampleQueryService<Long, TUser> {
}