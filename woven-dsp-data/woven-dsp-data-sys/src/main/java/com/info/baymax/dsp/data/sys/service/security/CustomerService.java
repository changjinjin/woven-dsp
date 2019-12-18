package com.info.baymax.dsp.data.sys.service.security;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.sys.entity.security.Customer;

public interface CustomerService extends BaseEntityService<Customer> {

	Customer findByTenantAndUsername(Long tenantId, String username);
}
