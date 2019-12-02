//package com.jusfoun.services.auth.server.service.impl;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.stereotype.Service;
//
//import com.jusfoun.common.base.cache.CacheConsts;
//import com.jusfoun.common.enums.types.UsingStatus;
//import com.jusfoun.common.message.exception.ServiceException;
//import com.jusfoun.common.message.result.ErrType;
//import com.jusfoun.common.mybatis.mapper.MyBaseMapper;
//import com.jusfoun.common.mybatis.mapper.MyIdableMapper;
//import com.jusfoun.common.mybatis.mapper.extension.BaseExtensionSelectMapper;
//import com.jusfoun.common.utils.ICollections;
//import com.jusfoun.services.auth.api.entity.SysPrivs;
//import com.jusfoun.services.auth.api.entity.SysRole;
//import com.jusfoun.services.auth.api.entity.SysRolePrivs;
//import com.jusfoun.services.auth.server.mapper.SysRoleMapper;
//import com.jusfoun.services.auth.server.mapper.SysRolePrivsMapper;
//import com.jusfoun.services.auth.server.service.SysRoleService;
//
///**
// * 说明：系统角色业务接口实现. <br>
// * 
// * @author yjw@jusfoun.com
// * @date 2018年7月23日 下午5:22:38
// */
//@Service
//public class SysRoleServiceImpl implements SysRoleService {
//
//	@Autowired
//	private SysRoleMapper sysRoleMapper;
//	
//	@Autowired
//	private SysRolePrivsMapper sysRolePrivsMapper;
//
//	@Override
//	public BaseExtensionSelectMapper<SysRole> getBaseExtensionSelectMapper() {
//		return sysRoleMapper;
//	}
//
//	@Override
//	public MyIdableMapper<SysRole> getMyIdableMapper() {
//		return sysRoleMapper;
//	}
//
//	@Override
//	public MyBaseMapper<SysRole> getMyBaseMapper() {
//		return sysRoleMapper;
//	}
//
//	@Override
//	public void saveWithAssociate(SysRole sysRole) throws ServiceException {
//		if (sysRole == null) {
//			throw new ServiceException(ErrType.SYSROLE_ENTITY_NULL);
//		}
//		try {
//			sysRole.setStatus(UsingStatus.ENABLE.getValue());
//			Date createTime = new Date();
//			sysRole.setCreateDate(createTime);
//			sysRole.setUpdateDate(createTime);
//			sysRoleMapper.insert(sysRole);
//
//			// 保存关系
//			Set<SysPrivs> sysPrivss = sysRole.getSysPrivss();
//			if (ICollections.hasElements(sysPrivss)) {
//				Long roleId = sysRole.getId();
//				List<SysRolePrivs> list = new ArrayList<SysRolePrivs>();
//				SysRolePrivs record = null;
//				for (SysPrivs t : sysPrivss) {
//					record = new SysRolePrivs();
//					record.setPrivsId(t.getId());
//					record.setRoleId(roleId);
//					list.add(record);
//				}
//				sysRolePrivsMapper.insertList(list);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ServiceException(ErrType.SYSROLE_SAVE_ERROR);
//		}
//	}
//
//	// 修改角色的时候需要同时修改用户的权限，所以需要同时清理权限的缓存信息
//	@CacheEvict(value = CacheConsts.CACHE_SECURITY, allEntries = true)
//	@Override
//	public void updateWithAssociate(SysRole sysRole) throws ServiceException {
//		if (sysRole == null) {
//			throw new ServiceException(ErrType.SYSROLE_ENTITY_NULL);
//		}
//		try {
//			sysRole.setUpdateDate(new Date());
//			sysRoleMapper.updateByPrimaryKeySelective(sysRole);
//
//			// 删除原来的关系
//			Long roleId = sysRole.getId();
//			SysRolePrivs record = new SysRolePrivs();
//			record.setRoleId(roleId);
//			sysRolePrivsMapper.delete(record);
//
//			Set<SysPrivs> sysPrivss = sysRole.getSysPrivss();
//			if (ICollections.hasElements(sysPrivss)) {
//				// 保存新关系
//				List<SysRolePrivs> list = new ArrayList<SysRolePrivs>();
//				for (SysPrivs t : sysPrivss) {
//					record = new SysRolePrivs();
//					record.setPrivsId(t.getId());
//					record.setRoleId(roleId);
//					list.add(record);
//				}
//				sysRolePrivsMapper.insertList(list);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ServiceException(ErrType.SYSROLE_UPDATE_ERROR);
//		}
//	}
//
//	// 删除角色的时候需要同时修改用户的权限，所以需要同时清理权限的缓存信息
//	@CacheEvict(value = CacheConsts.CACHE_SECURITY, allEntries = true)
//	@Override
//	public void deleteRoleWithPrivss(Long id) throws ServiceException {
//		try {
//			// 删除角色对应的权限关系
//			SysRolePrivs t = new SysRolePrivs();
//			t.setRoleId(id);
//			sysRolePrivsMapper.delete(t);
//
//			// 删除角色
//			sysRoleMapper.deleteByPrimaryKey(id);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ServiceException(ErrType.SYSROLE_DELETE_ERROR);
//		}
//	}
//}
