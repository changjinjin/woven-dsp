package com.merce.woven.dsp.access.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.merce.woven.common.base.service.entity.EntityClassServiceImpl;
import com.merce.woven.common.mybatis.mapper.MyBaseMapper;
import com.merce.woven.common.mybatis.mapper.base.BaseExampleMapper;
import com.merce.woven.dsp.access.platform.entity.User;
import com.merce.woven.dsp.access.platform.mybatis.mapper.UserMapper;
import com.merce.woven.dsp.access.platform.service.UserService;

@Service
public class UserServiceImpl extends EntityClassServiceImpl<User> implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public BaseExampleMapper<User> getBaseExampleMapper() {
		return userMapper;
	}

	@Override
	public MyBaseMapper<User> getMyBaseMapper() {
		return userMapper;
	}

}
