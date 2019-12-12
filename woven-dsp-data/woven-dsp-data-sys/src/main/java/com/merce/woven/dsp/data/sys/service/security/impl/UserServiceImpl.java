package com.merce.woven.dsp.data.sys.service.security.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.passay.PasswordData;
import org.passay.PasswordData.SourceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.merce.woven.common.entity.preprocess.annotation.PreInsert;
import com.merce.woven.common.entity.preprocess.annotation.PreUpdate;
import com.merce.woven.common.entity.preprocess.annotation.Preprocess;
import com.merce.woven.common.enums.types.YesNoType;
import com.merce.woven.common.jpa.criteria.query.QueryObject;
import com.merce.woven.common.jpa.page.Page;
import com.merce.woven.common.message.exception.ServiceException;
import com.merce.woven.common.message.result.ErrType;
import com.merce.woven.common.mybatis.mapper.MyIdableMapper;
import com.merce.woven.common.saas.SaasContext;
import com.merce.woven.common.service.entity.EntityClassServiceImpl;
import com.merce.woven.common.utils.ICollections;
import com.merce.woven.dsp.data.sys.constant.CacheNames;
import com.merce.woven.dsp.data.sys.crypto.check.PasswordChecker;
import com.merce.woven.dsp.data.sys.entity.security.Role;
import com.merce.woven.dsp.data.sys.entity.security.User;
import com.merce.woven.dsp.data.sys.entity.security.UserRoleRef;
import com.merce.woven.dsp.data.sys.initialize.InitConfig;
import com.merce.woven.dsp.data.sys.mybatis.mapper.security.RoleMapper;
import com.merce.woven.dsp.data.sys.mybatis.mapper.security.UserMapper;
import com.merce.woven.dsp.data.sys.mybatis.mapper.security.UserRoleRefMapper;
import com.merce.woven.dsp.data.sys.service.security.UserService;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl extends EntityClassServiceImpl<User> implements UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private UserRoleRefMapper userRoleRefMapper;

	@Autowired
	private InitConfig initConfig;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PasswordChecker passwordChecker;

	@Override
	public MyIdableMapper<User> getMyIdableMapper() {
		return userMapper;
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SECURITY, key = "'security_cache_user_'+#tenantId+'_'+#loginId", unless = "#result==null")
	@Override
	public User findByTenantAndLoginId(Long tenantId, String loginId) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId);
		params.put("loginId", loginId);
		return userMapper.selectOneWithRoles(params);
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Preprocess
	@Transactional
	public User save(@PreInsert User t) {
		if (existsByTenantIdAndName(SaasContext.getCurrentTenantId(), t.getLoginId())) {
			throw new ServiceException(ErrType.ENTITY_EXIST, "同名用户已经存在");
		}
		LocalDateTime now = LocalDateTime.now();
		t.setPwdExpiredTime(Date.from(now.plusMonths(3).atZone(ZoneId.systemDefault()).toInstant()));
		t.setAccountExpiredTime(Date.from(now.plusMonths(6).atZone(ZoneId.systemDefault()).toInstant()));
		if (t.getAdmin() == null) {
			t.setAdmin(YesNoType.NO.getValue());
		}
		t.setEnabled(YesNoType.YES.getValue());
		String password = t.getPassword();
		if (StringUtils.isEmpty(password)) {
			password = initConfig.getPassword();
		}
		t.setPassword(passwordEncoder.encode(password));

		// 保存用户信息
		insertSelective(t);

		// 保存更新中间表信息
		updateUserRoleRefs(t, t.getRoles());
		return t;
	}

	private void updateUserRoleRefs(User t, List<Role> roles) {
		// 保存关联的角色关系
		if (ICollections.hasElements(roles)) {
			// 清理中间表数据
			userRoleRefMapper.deleteByUserId(t.getId());
			// 保存中间表数据
			for (Role r : roles) {
				if (r.getId() == null) {
					userRoleRefMapper.insertSelective(new UserRoleRef(t.getId(), r.getId()));
				}
			}
		}
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Preprocess
	@Override
	public User update(@PreUpdate User t) {
		// 更新用户信息
		updateByPrimaryKeySelective(t);
		// 保存更新中间表信息
		updateUserRoleRefs(t, t.getRoles());
		return t;
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public int resetPwd(Long[] ids) {
		if (ids != null && ids.length > 0) {
			for (Long id : ids) {
				User t = new User();
				t.setId(id);
				t.setPassword(passwordEncoder.encode(initConfig.getPassword()));
				updateByPrimaryKeySelective(t);
			}
			return ids.length;
		}
		return 1;
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public int resetStatus(List<User> list) {
		if (ICollections.hasElements(list)) {
			for (User user : list) {
				updateByPrimaryKeySelective(user);
			}
			return list.size();
		}
		return 1;
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public int changePwd(String oldPass, String newPass) {
		User merceUser = selectByPrimaryKey(SaasContext.getCurrentUserId());
		if (merceUser == null) {
			throw new ServiceException(ErrType.ENTITY_NOT_EXIST, "密码修改失败，用户不存在！");
		}

		// 密码加密匹配
		if (!passwordEncoder.matches(oldPass, merceUser.getPassword())) {
			throw new BadCredentialsException("密码修改失败，原密码错误！");
		}

		// 密码格式检查
		if (initConfig.isPwdStrict()) {
			PasswordData passwordData = new PasswordData(SaasContext.getCurrentUsername(), newPass);
			passwordData.setPasswordReferences(new SourceReference(oldPass));
			passwordChecker.check(passwordData);
		}

		merceUser.setPassword(passwordEncoder.encode(newPass));
		merceUser.setPwdExpiredTime(
				Date.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()).toInstant()));
		updateByPrimaryKeySelective(merceUser);
		return 1;
	}

	@CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
	@Override
	public int deleteByIds(Long[] ids) {
		List<User> list = findByIds(SaasContext.getCurrentTenantId(), ids);
		for (User user : list) {
			if (YesNoType.YES.equalsTo(user.getEnabled())) {
				throw new ServiceException(ErrType.ENTITY_DELETE_ERROR, "删除用户前请将用户停用！");
			}
		}
		/*
		 * boolean clearUserAllData = wovenAppRestClient.clearUserAllData(ids); if (clearUserAllData) { for (String id :
		 * ids) { userRoleRefMapper.deleteByUserId(id); } deleteByPrimaryKeys(ids); }
		 */
		deleteByPrimaryKeys(ids);
		return ids.length;
	}

	@Override
	public Page<User> findObjectPage(QueryObject queryObject) {
		Page<User> page = UserService.super.findObjectPage(queryObject);
		if (page != null) {
			fetchUsersRoles(page.getContent());
		}
		return page;
	}

	/**
	 * 查询角色对应的权限列表
	 *
	 * @param list 角色列表
	 */
	private void fetchUsersRoles(List<User> list) {
		if (ICollections.hasElements(list)) {
			for (User u : list) {
				fetchUserRoles(u);
			}
		}
	}

	/**
	 * 查询用户对应的角色列表
	 *
	 * @param u 用户
	 */
	private void fetchUserRoles(User u) {
		if (u != null) {
			u.setRoles(roleMapper.selectOnlyRoleByUserId(u.getId()));
		}
	}

}
