package com.info.baymax.common.persistence.mybatis.mybatis.service;

import com.info.baymax.common.persistence.mybatis.mybatis.entity.TUser;
import com.info.baymax.common.persistence.service.BaseIdableService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQueryService;

public interface TUserService extends BaseIdableService<Long, TUser>, ExampleQueryService<TUser> {
}
