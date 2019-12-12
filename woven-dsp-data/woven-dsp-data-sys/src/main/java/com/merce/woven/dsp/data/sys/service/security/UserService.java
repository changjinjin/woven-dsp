package com.merce.woven.dsp.data.sys.service.security;

import java.util.List;

import com.merce.woven.common.entity.base.BaseEntityService;
import com.merce.woven.common.jpa.criteria.QueryObjectCriteriaService;
import com.merce.woven.dsp.data.sys.entity.security.User;

public interface UserService extends BaseEntityService<User>, QueryObjectCriteriaService<User> {

	/**
	 * 根据租户和用户名查询用户信息
	 *
	 * @param tenantId 租户ID
	 * @param loginId  登录ID
	 * @return 用户信息
	 */
	User findByTenantAndLoginId(Long tenantId, String loginId);

	/**
	 * 重置用户密码
	 *
	 * @param ids 需要重置密码的用户
	 * @return 修改成功条数
	 */
	int resetPwd(Long[] ids);

	/**
	 * 修改用户启用停用状态
	 *
	 * @param list 用户列表
	 * @return 修改成功条数
	 */
	int resetStatus(List<User> list);

	/**
	 * 修改用户密码
	 *
	 * @param oldPass 旧的密码
	 * @param newPass 新的密码
	 * @return 修改状态
	 */
	int changePwd(String oldPass, String newPass);

	/**
	 * 根据ID批量删除
	 *
	 * @param ids ID集合
	 * @return 删除条数
	 */
	int deleteByIds(Long[] ids);
}
