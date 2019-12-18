package com.info.baymax.dsp.data.sys.service.security.impl;

import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.CustomerMapper;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class CustomerServiceImpl extends EntityClassServiceImpl<Customer> implements CustomerService {

	@Autowired
	private CustomerMapper consumerMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public MyIdableMapper<Customer> getMyIdableMapper() {
		return consumerMapper;
	}

	@Override
	public Customer findByTenantAndUsername(Long tenantId, String username) {
		return selectOne(ExampleQuery.builder(getEntityClass()).fieldGroup().andEqualTo("tenantId", tenantId)
				.andEqualTo("username", username).end());
	}

	@Override
	public Customer save(Customer t) {
		if (t == null) {
			throw new ServiceException(ErrType.BAD_REQUEST, "保存对象不能为空");
		}

		String username = t.getUsername();
		if (existsByTenantIdAndName(SaasContext.getCurrentTenantId(), username)) {
			throw new ServiceException(ErrType.ENTITY_EXIST, "同名的消费者信息已经存在");
		}

		t.setEnabled(YesNoType.YES.getValue());
		t.setPassword(passwordEncoder.encode(t.getPassword()));
		return CustomerService.super.save(t);
	}

	@Override
	public Customer update(Customer t) {
		if (t == null) {
			throw new ServiceException(ErrType.BAD_REQUEST, "修改对象不能为空");
		}

		// 不能修改账号和密码
		t.setUsername(null);
		t.setPassword(null);
		return CustomerService.super.update(t);
	}

}
