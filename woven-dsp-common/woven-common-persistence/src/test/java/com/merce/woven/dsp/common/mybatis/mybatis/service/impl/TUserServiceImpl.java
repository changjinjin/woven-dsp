package com.merce.woven.dsp.common.mybatis.mybatis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.merce.woven.dsp.common.mybatis.mybatis.entity.TUser;
import com.merce.woven.dsp.common.mybatis.mybatis.mapper.TUserMapper;
import com.merce.woven.dsp.common.mybatis.mybatis.service.TUserService;

@Service
public class TUserServiceImpl extends EntityClassServiceImpl<TUser> implements TUserService {

    @Autowired
    private TUserMapper tUserMapper;

    public MyIdableMapper<TUser> getMyIdableMapper() {
        return tUserMapper;
    }

    public MyBaseMapper<TUser> getMyBaseMapper() {
        return tUserMapper;
    }

    public BaseExampleMapper<TUser> getBaseExampleMapper() {
        return tUserMapper;
    }
}
