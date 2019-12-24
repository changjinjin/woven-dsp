package com.info.baymax.dsp.data.sys.service.security;

import com.info.baymax.common.entity.base.BaseMaintableService;
import com.info.baymax.dsp.data.sys.entity.security.Role;

import java.util.List;

public interface RoleService extends BaseMaintableService<Role> {

	/**
	 * 批量修改角色启用和停用状态
	 *
	 * @param list 角色列表
	 * @return 修改结果
	 */
	int resetStatus(List<Role> list);

	/**
	 * 根据角色ID查询
	 *
	 * @param id 角色ID
	 * @return 角色信息，包含对应的权限信息
	 */
	Role selectWithPermissionsAndResourcesRefsById(String id);

	/**
	 * 更新
	 * 
	 * @param t
	 * @return
	 */
	Role updateRrrs(Role t);
}
