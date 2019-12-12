package com.merce.woven.dsp.data.sys.service.security;

import com.merce.woven.common.entity.base.BaseEntityService;
import com.merce.woven.common.jpa.criteria.QueryObjectCriteriaService;
import com.merce.woven.dsp.data.sys.entity.security.Tenant;
import com.merce.woven.dsp.data.sys.entity.security.TenantRegisterBean;
import com.merce.woven.dsp.data.sys.entity.security.User;
import com.merce.woven.dsp.data.sys.initialize.InitConfig;

public interface TenantService extends BaseEntityService<Tenant>, QueryObjectCriteriaService<Tenant> {

	int countByName(String name);

	Tenant findByName(String tenant);

	Tenant createTenant(InitConfig initConfig, TenantRegisterBean tenant, User creater);

	Tenant updateTenant(InitConfig initConfig, TenantRegisterBean tenant, User user);

	void disableTenants(String[] ids);

	void enableTenants(String[] ids);

}
