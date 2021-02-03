package com.info.baymax.dsp.data.sys.service.security.impl;

import com.info.baymax.common.core.enums.types.YesNoType;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.queryapi.exception.ServiceException;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.result.ErrType;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.constant.CacheNames;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.PermissionMapper;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.RolePermissionRefMapper;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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

	public Permission findByCode(String code) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo("code", code)));
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public Permission save(Permission t) {
		Permission exists = findByCode(t.getCode());
		if (exists != null) {
			throw new ServiceException(ErrType.ENTITY_EXIST, "相同编码的权限[" + t.getCode() + "]已经存在！");
		}
		t.setClientId(SaasContext.getCurrentClienId());
		t.setEnabled(YesNoType.YES.getValue());
		t.setOrder(selectMaxOrder() + 1);
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
	public List<String> findAllAuthorities() {
		List<Permission> permissions = selectAll();
		if (ICollections.hasElements(permissions)) {
			return permissions.stream().filter(t -> t.getEnabled() > 0).map(t -> t.getUrl())
					.collect(Collectors.toList());
		}
		return null;
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result==null")
	@Override
	public List<Permission> findRootsTree() {
		List<Permission> allList = selectAll();
		return fetchTree(findRoots(allList), findNotRoots(allList));
	}

	@Override
	public int selectMaxOrder() {
		return permissionMapper.selectMaxOrder();
	}
}
