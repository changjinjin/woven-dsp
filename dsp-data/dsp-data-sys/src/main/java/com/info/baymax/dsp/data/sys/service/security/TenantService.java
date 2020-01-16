package com.info.baymax.dsp.data.sys.service.security;

import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.TenantRegisterBean;
import com.info.baymax.dsp.data.sys.initialize.InitConfig;

public interface TenantService extends BaseMaintableService<Tenant> {

	int countByName(String name);

	Tenant findByName(String tenant);

	Tenant createTenant(InitConfig initConfig, TenantRegisterBean tenant);

	Tenant updateTenant(InitConfig initConfig, TenantRegisterBean tenant);

	void disableTenants(String[] ids);

	void enableTenants(String[] ids);

}
