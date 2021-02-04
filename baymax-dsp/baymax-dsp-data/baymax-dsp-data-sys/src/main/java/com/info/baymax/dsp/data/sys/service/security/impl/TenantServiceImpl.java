package com.info.baymax.dsp.data.sys.service.security.impl;

import com.info.baymax.common.core.enums.types.YesNoType;
import com.info.baymax.common.core.exception.ServiceException;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.dsp.data.sys.constant.CacheNames;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.TenantRegisterBean;
import com.info.baymax.dsp.data.sys.initialize.InitConfig;
import com.info.baymax.dsp.data.sys.initialize.TenantInitializer;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.TenantMapper;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional(rollbackOn = Exception.class)
public class TenantServiceImpl extends EntityClassServiceImpl<Tenant> implements TenantService {

	@Autowired
	private TenantMapper tenantMapper;
	@Autowired
	private TenantInitializer tenantInitializer;

	@Override
	public MyIdableMapper<Tenant> getMyIdableMapper() {
		return tenantMapper;
	}

	@Override
	public int countByName(String name) {
		return selectCount(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo("name", name)));
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SECURITY, key = "'security_cache_tenant_name_'+#name", unless = "#result==null")
	@Override
	public Tenant findByName(String name) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo("name", name)));
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	public Tenant createTenant(InitConfig initConfig, TenantRegisterBean tenant) {
		int count = countByName(tenant.getName());
		if (count > 0) {
			throw new ServiceException(ErrType.ENTITY_EXIST, "同名的租户已经存在");
		}

		Tenant rootTnt = findByName(TenantInitializer.INIT_ROOT_LOGINID);
		tenant.setTenantId(rootTnt.getId());
		tenant.setOwner(SaasContext.getCurrentUserId());
		tenant.setCreator(SaasContext.getCurrentUsername());
		tenant.setLastModifier(SaasContext.getCurrentUsername());
		tenant.setEnabled(YesNoType.YES.getValue());
		insertSelective(tenant);
		tenantInitializer.initTenantAdmin(initConfig, tenant, tenant.getAdminPassword());
		return tenant;
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	public Tenant updateTenant(InitConfig initConfig, TenantRegisterBean tenant) {
		tenant.setLastModifier(SaasContext.getCurrentUsername());
		tenant.setLastModifiedTime(new Date());
		updateByPrimaryKey(tenant);
		tenantInitializer.initTenantAdmin(initConfig, tenant, tenant.getAdminPassword());
		return tenant;
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public void disableTenants(String[] ids) {
		enableOrDisableTenants(ids, 0);
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public void enableTenants(String[] ids) {
		enableOrDisableTenants(ids, 1);
	}

	private void enableOrDisableTenants(String[] ids, Integer enable) {
		for (String id : ids) {
			Tenant tenant = selectByPrimaryKey(id);
			if (tenant != null) {
				tenant.setEnabled(enable);
				tenant.setLastModifiedTime(new Date());
				updateByPrimaryKeySelective(tenant);
			}
		}
	}
}
