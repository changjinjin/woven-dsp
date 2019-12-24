package com.info.baymax.dsp.data.sys.service.security;

import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.dsp.data.sys.entity.security.Customer;

import java.util.List;

public interface CustomerService extends BaseMaintableService<Customer> {

    Customer findByTenantAndUsername(String tenantId, String username);

    int changePwd(String oldPass, String newPass, boolean pwdStrict);

    int resetPwd(String[] ids, String password);

    int resetStatus(List<Customer> list);
}
