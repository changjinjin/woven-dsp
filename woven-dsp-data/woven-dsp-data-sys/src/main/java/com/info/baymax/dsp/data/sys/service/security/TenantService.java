package com.info.baymax.dsp.data.sys.service.security;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.TenantRegisterBean;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.initialize.InitConfig;

public interface TenantService extends BaseEntityService<Tenant>, QueryObjectCriteriaService<Tenant> {

	int countByName(String name);

	Tenant findByName(String tenant);

	Tenant createTenant(InitConfig initConfig, TenantRegisterBean tenant, User creater);

	Tenant updateTenant(InitConfig initConfig, TenantRegisterBean tenant, User user);

	void disableTenants(String[] ids);

	void enableTenants(String[] ids);

}
