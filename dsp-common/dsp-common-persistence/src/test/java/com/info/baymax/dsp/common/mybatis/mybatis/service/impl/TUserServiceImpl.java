package com.info.baymax.dsp.common.mybatis.mybatis.service.impl;

import com.info.baymax.common.service.BaseIdableService;
import com.info.baymax.common.service.criteria.ExampleQueryService;
import com.info.baymax.dsp.common.mybatis.mybatis.entity.TUser;

public interface TUserServiceImpl extends BaseIdableService<Long, TUser>, ExampleQueryService<TUser> {
}
