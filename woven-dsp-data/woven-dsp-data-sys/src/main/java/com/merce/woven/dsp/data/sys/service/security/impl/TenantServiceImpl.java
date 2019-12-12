package com.merce.woven.dsp.data.sys.service.security.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.merce.woven.common.enums.types.YesNoType;
import com.merce.woven.common.message.exception.ServiceException;
import com.merce.woven.common.message.result.ErrType;
import com.merce.woven.common.mybatis.mapper.MyIdableMapper;
import com.merce.woven.common.service.criteria.example.ExampleQuery;
import com.merce.woven.common.service.entity.EntityClassServiceImpl;
import com.merce.woven.dsp.data.sys.constant.CacheNames;
import com.merce.woven.dsp.data.sys.entity.security.Tenant;
import com.merce.woven.dsp.data.sys.entity.security.TenantRegisterBean;
import com.merce.woven.dsp.data.sys.entity.security.User;
import com.merce.woven.dsp.data.sys.initialize.InitConfig;
import com.merce.woven.dsp.data.sys.initialize.TenantInitializer;
import com.merce.woven.dsp.data.sys.mybatis.mapper.security.TenantMapper;
import com.merce.woven.dsp.data.sys.service.security.TenantService;

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
				.fieldGroup()//
				.andEqualTo("name", name)//
				.end());
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SECURITY, key = "'security_cache_tenant_name_'+#name", unless = "#result==null")
	@Override
	public Tenant findByName(String name) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("name", name)//
				.end());
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	public Tenant createTenant(InitConfig initConfig, TenantRegisterBean tenant, User creater) {
		int count = countByName(tenant.getName());
		if (count > 0) {
			throw new ServiceException(ErrType.ENTITY_EXIST, "同名的租户已经存在");
		}

		Tenant rootTnt = findByName(TenantInitializer.INIT_ROOT_LOGINID);
		tenant.setTenantId(rootTnt.getId());
		tenant.setOwner(creater.getId());
		tenant.setCreator(creater.getUsername());
		tenant.setLastModifier(creater.getUsername());
		tenant.setEnabled(YesNoType.YES.getValue());
		insertSelective(tenant);
		tenantInitializer.initTenantAdminAndPerms(initConfig, tenant, tenant.getAdminPassword());
		return tenant;
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	public Tenant updateTenant(InitConfig initConfig, TenantRegisterBean tenant, User user) {
		tenant.setLastModifier(user.getUsername());
		tenant.setLastModifiedTime(new Date());
		updateByPrimaryKey(tenant);
		tenantInitializer.initTenantAdminAndPerms(initConfig, tenant, tenant.getAdminPassword());
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
