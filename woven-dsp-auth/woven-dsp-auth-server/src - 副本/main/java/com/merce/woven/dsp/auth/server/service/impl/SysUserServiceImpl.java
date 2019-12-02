//package com.jusfoun.services.auth.server.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import com.jusfoun.common.base.cache.CacheConsts;
//import com.jusfoun.common.message.exception.ServiceException;
//import com.jusfoun.common.message.result.ErrType;
//import com.jusfoun.common.mybatis.mapper.MyBaseMapper;
//import com.jusfoun.common.mybatis.mapper.MyIdableMapper;
//import com.jusfoun.common.mybatis.mapper.extension.BaseExtensionSelectMapper;
//import com.jusfoun.common.utils.ICollections;
//import com.jusfoun.services.auth.api.entity.SysRole;
//import com.jusfoun.services.auth.api.entity.SysRoleUser;
//import com.jusfoun.services.auth.api.entity.SysUser;
//import com.jusfoun.services.auth.server.mapper.SysRoleUserMapper;
//import com.jusfoun.services.auth.server.mapper.SysUserMapper;
//import com.jusfoun.services.auth.server.service.SysUserService;
//
///**
// * 说明：系统用户信息管理业务接口实现. <br>
// * 
// * @author yjw@jusfoun.com
// * @date 2017年11月24日 下午7:00:05
// */
//@Service
//public class SysUserServiceImpl implements SysUserService {
//
//	@Autowired
//	private SysUserMapper sysUserMapper;
//	
//	@Autowired
//	private SysRoleUserMapper sysRoleUserMapper;
//
//	@Override
//	public BaseExtensionSelectMapper<SysUser> getBaseExtensionSelectMapper() {
//		return sysUserMapper;
//	}
//
//	@Override
//	public MyIdableMapper<SysUser> getMyIdableMapper() {
//		return sysUserMapper;
//	}
//
//	@Override
//	public MyBaseMapper<SysUser> getMyBaseMapper() {
//		return sysUserMapper;
//	}
//
//	@Override
//	public int insertWithCache(SysUser sysUser) {
//		return insertSelective(sysUser);
//	}
//
//	@CacheEvict(value = CacheConsts.CACHE_SECURITY, allEntries = true)
//	@Override
//	public int deleteWithRoles(Long id) throws ServiceException {
//		try {
//			// 删除用户角色
//			SysRoleUser t = new SysRoleUser();
//			t.setUserId(id);
//			sysRoleUserMapper.delete(t);
//
//			// 删除用户信息
//			return sysUserMapper.deleteByPrimaryKey(id);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ServiceException(ErrType.SYSUSER_DELETE_ERROR);
//		}
//	}
//
//	@CacheEvict(value = CacheConsts.CACHE_SECURITY, allEntries = true)
//	@Override
//	public void updateByPrimaryKeySelectiveWithCache(SysUser sysUser) {
//		sysUser.setPassword(null);
//		updateByPrimaryKeySelective(sysUser);
//	}
//
//	@CacheEvict(value = CacheConsts.CACHE_SECURITY, allEntries = true)
//	@Override
//	public void modifyRoles(SysUser sysUser) throws ServiceException {
//		SysRoleUser record = new SysRoleUser();
//		record.setUserId(sysUser.getId());
//		sysRoleUserMapper.delete(record);
//
//		Set<SysRole> sysRoles = sysUser.getSysRoles();
//		if (ICollections.hasElements(sysRoles)) {
//			List<SysRoleUser> list = new ArrayList<SysRoleUser>();
//			for (SysRole sysRole : sysRoles) {
//				record = new SysRoleUser();
//				record.setUserId(sysUser.getId());
//				record.setRoleId(sysRole.getId());
//				list.add(record);
//			}
//			sysRoleUserMapper.insertList(list);
//		}
//	}
//
//	@Cacheable(value = CacheConsts.CACHE_SECURITY, key = "'security_cache_sysuser_' + #username", unless = "#result == null")
//	@Override
//	public SysUser selectByUsername(String username) throws ServiceException {
//		return sysUserMapper.selectByUsername(username);
//	}
//
//	@Cacheable(value = CacheConsts.CACHE_SECURITY, key = "'security_cache_sysuser_' + #id", unless = "#result == null")
//	@Override
//	public SysUser selectPKWithCache(Long id) throws ServiceException {
//		return selectExtensionPK(id);
//	}
//
//}
