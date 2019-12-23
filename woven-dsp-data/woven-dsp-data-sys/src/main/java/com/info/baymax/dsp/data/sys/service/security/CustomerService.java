package com.info.baymax.dsp.data.sys.service.security;

import java.util.List;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.sys.entity.security.Customer;

public interface CustomerService extends BaseEntityService<Customer> {

    Customer findByTenantAndUsername(Long tenantId, String username);

    int changePwd(String oldPass, String newPass, boolean pwdStrict);

    int resetPwd(Long[] ids, String password);

    int resetStatus(List<Customer> list);
}
