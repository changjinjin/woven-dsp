package com.info.baymax.dsp.common.mybatis.mybatis.service;

import com.info.baymax.common.service.BaseIdableService;
import com.info.baymax.common.service.criteria.example.ExampleQueryService;
import com.info.baymax.dsp.common.mybatis.mybatis.entity.TUser;

public interface TUserService extends BaseIdableService<Long, TUser>, ExampleQueryService<TUser> {
}
