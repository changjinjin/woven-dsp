package com.info.baymax.dsp.data.sys.initialize;

import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.sys.entity.security.*;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.RolePermissionRefMapper;
import com.info.baymax.dsp.data.sys.service.security.PermOperationRefService;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.dsp.data.sys.service.security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统启动时初始化系统中租户相关数据：包含租户下管理员账户、权限列表等数据
 *
 * @author jingwei.yang
 * @date 2019年6月18日 下午3:02:59
 */
@Component
@Slf4j
public class TenantInitializer {

	// 初始化管理员账户
	public static final String INIT_ROOT_LOGINID = "root";
	public static final String INIT_ADMIN_LOGINID = "admin";
	// 初始化租户账户
	String[] initTenants = new String[] { "root", "default" };

	private static int order = 0;

	private PermsParser permsParser = new PermsParser();
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TenantService tenantService;
	@Autowired
	private UserService userService;
	@Autowired
	private RolePermissionRefMapper rolePermissionRefMapper;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private PermOperationRefService permOperationRefService;

	// @Transactional
	public void initAllTenantAdminAndPerms(InitConfig initConfig) {
		if (!initConfig.isEnabled()) {
			log.warn("System parameter 'security.init.enable = false', skip the steps of initializing tenants!");
			return;
		}

		// 初始化系统公共菜单列表
		initPerms(initConfig);

		// 初始化系统租户
		for (String tenant : initTenants) {
			initTenant(initConfig, tenant);
		}

		// 初始化租户管理员和权限
		List<Tenant> list = tenantService.selectAll();
		if (ICollections.hasElements(list)) {
			for (Tenant tenant : list) {
				initTenantAdmin(initConfig, tenant, null);
			}
		}
	}

	private void initTenant(InitConfig initConfig, String tenant) {
		Tenant rootTenant = null;
		try {
			rootTenant = tenantService.findByName(tenant);
			if (rootTenant == null) {
				tenantService.insertSelective(new Tenant(tenant, Arrays.asList("default")));
			}
		} catch (Exception e) {
			log.warn("save tenant info:{} error, cause by:{}", JsonBuilder.getInstance().toJson(rootTenant), e.getMessage());
		}
	}

	public User initTenantAdmin(InitConfig initConfig, Tenant tenant, String initAdminPassword) {
		return initUser(tenant,
				INIT_ROOT_LOGINID.equalsIgnoreCase(tenant.getName()) ? INIT_ROOT_LOGINID : initConfig.getUsername(),
				StringUtils.defaultString(initAdminPassword, initConfig.getPassword()));
	}

	private User initUser(Tenant tenant, String username, String initPassword) {
		User user = null;
		try {
			user = userService.findByTenantAndUsername(tenant.getId(), username);
			if (user == null) {
				user = User.apply(username, passwordEncoder.encode(initPassword), true);
				user.setResourceQueues(tenant.getResourceQueues());
				user.setHdfsSpaceQuota(tenant.getHdfsSpaceQuota());
				userService.insertSelective(user);
			} else if (!user.admin()) {
				user.setAdmin(YesNoType.YES.getValue());
				user.setResourceQueues(tenant.getResourceQueues());
				user.setHdfsSpaceQuota(tenant.getHdfsSpaceQuota());
				userService.updateByPrimaryKeySelective(user);
			} else if (StringUtils.isNotEmpty(initPassword)) {
				user.setPassword(passwordEncoder.encode(initPassword));
				userService.updateByPrimaryKeySelective(user);
			}
		} catch (Exception e) {
			log.warn("save user info:{} error, cause by:{}",JsonBuilder.getInstance().toJson(user), e.getMessage());
		}
		return user;
	}

	private void foramtOldDataOfPerms() {
		List<RolePermissionRef> allRefs = rolePermissionRefMapper.selectAll();
		if (ICollections.hasElements(allRefs)) {
			// 更新老的关联关系，Permission Id改为Permission Code，新的 Permission表中使用code作为主键
			for (RolePermissionRef ref : allRefs) {
				Permission perm = permissionService.selectByPrimaryKey(ref.getPermissionId());
				if (perm != null) {
					rolePermissionRefMapper.updatePermissionId(perm.getCode(), ref.getRoleId(), ref.getPermissionId());
				} else {
					rolePermissionRefMapper.delete(ref);
				}
			}
		}
		// 删除所有的Permission数据，后面重新录入
		permissionService.deleteAll();
	}

	private void initPerms(InitConfig initConfig) {
		foramtOldDataOfPerms();
		PermRoots roots = permsParser.getInitPerms(initConfig.getPermsFile());
		if (roots != null) {
			order = 0;
			for (Permission t : roots.getRoots()) {
				savePerm(t);
			}
		}
	}

	private void savePerm(Permission t) {
		try {
			Permission exists = permissionService.findByCode(t.getCode());
			List<Permission> children = t.getChildren();
			if (exists == null) {
				if (t.getEnabled() == null) {
					t.setEnabled(YesNoType.YES.getValue());
				}
				t.setOrder(++order);
				permissionService.insertSelective(t);
				initPermOperationRefs(t);
			} else {
				exists.setEnabled(t.getEnabled() == null ? YesNoType.YES.getValue() : t.getEnabled());
				exists.setCode(t.getCode());
				exists.setName(t.getName());
				exists.setEnName(t.getEnName());
				exists.setType(t.getType());
				exists.setUrl(t.getUrl());
				exists.setIcon(t.getIcon());
				exists.setRoute(t.getRoute());
				exists.setOrder(++order);
				permissionService.updateByPrimaryKeySelective(exists);

				exists.setOperations(t.getOperations());
				initPermOperationRefs(exists);
			}
			if (ICollections.hasElements(children)) {
				children.forEach(child -> {
					if (exists == null) {
						child.setParentId(t.getId());
					} else {
						child.setParentId(exists.getId());
					}
					savePerm(child);
				});
			}
		} catch (Exception e) {
			log.warn("save permission info:{} error, cause by:{}", JsonBuilder.getInstance().toJson(t), e.getMessage());
		}
	}

	// 保存菜单与rest api的关联关系
	public void initPermOperationRefs(Permission perm) {
		Set<RestOperation> operations = perm.getOperations();
		if (ICollections.hasElements(operations)) {
			List<PermOperationRef> list = operations.stream().map(t -> new PermOperationRef(perm.getId(), t.getId()))
					.collect(Collectors.toList());
			// 删掉这里指定
			permOperationRefService.deleteList(list);
			permOperationRefService.insertList(list);
		}
	}

}
