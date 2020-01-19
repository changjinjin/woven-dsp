package com.info.baymax.dsp.data.sys.service.security.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.constant.CacheNames;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.PermissionMapper;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.RolePermissionRefMapper;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;

@Service
@Transactional(rollbackOn = Exception.class)
public class PermissionServiceImpl extends EntityClassServiceImpl<Permission> implements PermissionService {

	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private RolePermissionRefMapper rolePermissionRefMapper;

	@Override
	public MyIdableMapper<Permission> getMyIdableMapper() {
		return permissionMapper;
	}

	public Permission findByClientIdAndCode(String clientId, String code) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo("clientId", clientId)//
				.andEqualTo("code", code)//
				.end());
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public Permission save(Permission t) {
		Permission exists = findByClientIdAndCode(SaasContext.getCurrentClienId(), t.getCode());
		if (exists != null) {
			throw new ServiceException(ErrType.ENTITY_EXIST, "相同编码的权限[" + t.getCode() + "]已经存在！");
		}
		t.setClientId(SaasContext.getCurrentClienId());
		t.setEnabled(YesNoType.YES.getValue());
		t.setOrder(selectMaxOrder(SaasContext.getCurrentClienId()) + 1);
		insertSelective(t);
		return t;
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
	public int deleteOnCascadeById(String id) {
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
	public int selectMaxOrder(String clientId) {
		return permissionMapper.selectMaxOrder(clientId);
	}
}