package com.merce.woven.dsp.data.sys.service.security.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.merce.woven.common.enums.types.YesNoType;
import com.merce.woven.common.message.exception.ServiceException;
import com.merce.woven.common.message.result.ErrType;
import com.merce.woven.common.mybatis.mapper.MyIdableMapper;
import com.merce.woven.common.saas.SaasContext;
import com.merce.woven.common.service.criteria.example.ExampleQuery;
import com.merce.woven.common.service.entity.EntityClassServiceImpl;
import com.merce.woven.common.utils.ICollections;
import com.merce.woven.dsp.data.sys.constant.CacheNames;
import com.merce.woven.dsp.data.sys.entity.security.Permission;
import com.merce.woven.dsp.data.sys.mybatis.mapper.security.PermissionMapper;
import com.merce.woven.dsp.data.sys.mybatis.mapper.security.RolePermissionRefMapper;
import com.merce.woven.dsp.data.sys.service.security.PermissionService;
import com.merce.woven.dsp.data.sys.service.security.TenantService;

@Service
@Transactional(rollbackOn = Exception.class)
public class PermissionServiceImpl extends EntityClassServiceImpl<Permission> implements PermissionService {

	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private RolePermissionRefMapper rolePermissionRefMapper;
	@Autowired
	private TenantService tenantService;

	@Override
	public MyIdableMapper<Permission> getMyIdableMapper() {
		return permissionMapper;
	}

	public Permission findByTenantIdAndCode(String tenantId, String code) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("tenantId", tenantId)//
				.andEqualTo("code", code)//
				.end());
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public Permission save(Permission t) {
		Permission exists = existsByTenantIdAndCode(SaasContext.getCurrentTenantId(), t.getCode());
		if (exists != null) {
			throw new ServiceException(ErrType.ENTITY_EXIST, "相同编码的权限[" + t.getCode() + "]已经存在！");
		}
		t.setEnabled(YesNoType.YES.getValue());
		t.setOrder(selectMaxOrder(SaasContext.getCurrentTenantId()) + 1);
		insertSelective(t);
		return t;
	}

	@Override
	public Permission existsByTenantIdAndCode(Long tenantId, String code) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("tenantId", tenantId)//
				.andEqualTo("code", code)//
				.end());
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public Permission update(Permission t) {
		// 防止前端报文中包含空的parent字段，如：parent: {}
		if (t.getParentId() == null) {
			t.setParentId(null);
		}

		if (t.getId() == null) {
			t.setId(null);
			save(t);
		} else {
			updateByPrimaryKeySelective(t);
		}
		return t;
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public int delete(Permission t) {
		// 先删除与角色的中间表关系
		rolePermissionRefMapper.deleteByPermissionId(t.getId());

		// 先删除叶子节点
		List<Permission> children = t.getChildren();
		if (ICollections.hasElements(children)) {
			for (Permission c : children) {
				delete(c);
			}
		}

		// 再删除自己
		return deleteByPrimaryKey(t.getId());
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public int deleteOnCascadeById(Long id) {
		Permission t = permissionMapper.selectOneWithChildren(id);
		if (t != null) {
			delete(t);
		}
		return 1;
	}

	// @Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result ==
	// null")
	@Override
	public List<String> findAuthoritiesByClientId(String clientId) {
		List<Permission> permissions = findAllByClientId(clientId);
		if (ICollections.hasElements(permissions)) {
			return permissions.stream().filter(t -> t.getEnabled() > 0).map(t -> t.getUrl())
					.collect(Collectors.toList());
		}
		return null;
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result==null")
	@Override
	public List<Permission> findRootsTree() {
		List<Permission> allList = findAllByClientId(SaasContext.getCurrentClienId());
		return fetchTree(findRoots(allList), findNotRoots(allList));
	}

	private List<Permission> findAllByClientId(String clientId) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("clientId", clientId)//
				.end());
	}

	@Override
	public int selectMaxOrder(Long tenantId) {
		return permissionMapper.selectMaxOrder(tenantId);
	}
}
