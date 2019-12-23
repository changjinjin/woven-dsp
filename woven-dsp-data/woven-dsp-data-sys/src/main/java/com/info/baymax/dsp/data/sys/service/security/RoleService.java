package com.info.baymax.dsp.data.sys.service.security;

import java.util.List;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.sys.entity.security.Role;

public interface RoleService extends BaseEntityService<Role> {

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
	Role selectWithPermissionsAndResourcesRefsById(Long id);

	/**
	 * 更新
	 * 
	 * @param t
	 * @return
	 */
	Role updateRrrs(Role t);
}
