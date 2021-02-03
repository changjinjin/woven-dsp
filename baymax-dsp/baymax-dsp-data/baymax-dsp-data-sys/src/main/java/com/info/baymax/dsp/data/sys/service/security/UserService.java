package com.info.baymax.dsp.data.sys.service.security;

import com.info.baymax.common.persistence.entity.base.BaseMaintableService;
import com.info.baymax.dsp.data.sys.entity.security.User;

import java.util.List;

public interface UserService extends BaseMaintableService<User> {

    /**
     * 根据租户和用户名查询用户信息
     *
     * @param tenantId 租户ID
     * @param loginId  登录ID
     * @return 用户信息
     */
	User findByTenantAndUsername(String tenantId, String loginId);

    /**
     * 重置用户密码
     *
     * @param ids     需要重置密码的用户
     * @param initPwd 初始密码
     * @return 修改成功条数
     */
    int resetPwd(String[] ids, String initPwd);

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
     * @param oldPass   旧的密码
     * @param newPass   新的密码
     * @param pwdMode   密码校验模式
     * @return 修改状态
     */
    int changePwd(String oldPass, String newPass);

    /**
     * 根据ID批量删除
     *
     * @param ids ID集合
     * @return 删除条数
     */
    int deleteByIds(String[] ids);
}
