package com.info.baymax.dsp.data.sys.service.security.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.CustomerMapper;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;

@Service
@Transactional(rollbackOn = Exception.class)
public class CustomerServiceImpl extends EntityClassServiceImpl<Customer> implements CustomerService {

	@Autowired
	private CustomerMapper consumerMapper;

	@Override
	public MyIdableMapper<Customer> getMyIdableMapper() {
		return consumerMapper;
	}

	@Override
	public Customer findByTenantAndUsername(Long tenantId, String username) {
		return selectOne(ExampleQuery.builder(getEntityClass()).fieldGroup().andEqualTo("tenantId", tenantId)
				.andEqualTo("username", username).end());
	}

}
