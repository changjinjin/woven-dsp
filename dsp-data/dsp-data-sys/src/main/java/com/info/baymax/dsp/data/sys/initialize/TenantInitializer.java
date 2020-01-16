package com.info.baymax.dsp.data.sys.initialize;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.dsp.data.sys.service.security.UserService;

import lombok.extern.slf4j.Slf4j;

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

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TenantService tenantService;
	@Autowired
	private UserService userService;
	@Autowired
	private PermissionService permissionService;

	private SaasContext saasContext = null;

	// @Transactional
	public void initAllTenantAdminAndPerms(InitConfig initConfig) {
		if (!initConfig.isEnabled()) {
			log.warn("系统参数 security.init.enable=false,跳过初始化租户步骤！");
			return;
		}

		// 初始化系统租户
		for (String tenant : initTenants) {
			initTenant(tenant);
		}

		List<Tenant> list = tenantService.selectAll();
		if (ICollections.hasNoElements(list)) {
			return;
		}

		// 初始化租户管理员和权限
		for (Tenant tenant : list) {
			initTenantAdminAndPerms(initConfig, tenant, null);
		}
	}

	// @Transactional
	public void initTenantAdminAndPerms(InitConfig initConfig, Tenant tenant, String initAdminPassword) {
		User initAdmin = initAdmin(initConfig, tenant, initAdminPassword);

		// 一个系统字需要初始化一份权限数据即可
		if (INIT_ROOT_LOGINID.equals(tenant.getName())) {
			PermClients clients = null;
			try {
				clients = PermsParser.getInitPerms(initConfig.getPermsFile());
			} catch (JAXBException e) {
				log.error(e.getMessage(), e);
			}
			if (clients != null) {
				initPerms(tenant, initAdmin, clients);
			}
		}

	}

	private void initSaasContext(Tenant tenant, User user) {
		saasContext = SaasContext.getCurrentSaasContext();
		saasContext.setTenantId(tenant.getId());
		saasContext.setTenantName(tenant.getName());
		saasContext.setUserId(user.getId());
		saasContext.setUsername(user.getUsername());
	}

	@PreDestroy
	public void destorySaasContext() {
		SaasContext.clear();
	}

	private void initTenant(String tenant) {
		Tenant rootTenant = null;
		try {
			rootTenant = tenantService.findByName(tenant);
			if (rootTenant == null) {
				tenantService.insertSelective(new Tenant(tenant, Arrays.asList("default")));
			}
		} catch (Exception e) {
			log.warn("save tenant info:{} error, cause by:{}", JSON.toJSONString(rootTenant), e.getMessage());
		}
	}

	private User initAdmin(InitConfig initConfig, Tenant tenant, String initAdminPassword) {
		saasContext = SaasContext.getCurrentSaasContext();
		saasContext.setTenantId(tenant.getId());
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
			log.warn("save user info:{} error, cause by:{}", JSON.toJSONString(user), e.getMessage());
		}
		return user;
	}

	private void initPerms(Tenant tenant, User user, PermClients clients) {
		initSaasContext(tenant, user);
		order = 0;
		for (PermClient client : clients.getClients()) {
			List<Permission> roots = client.getPermissions();
			for (Permission t : roots) {
				t.setClientId(client.getClientId());
				t.setTenantId(tenant.getId());
				t.setOwner(user.getId());
				savePerm(t, client.getClientId());
			}
		}
		destorySaasContext();
	}

	private void savePerm(Permission t, String clientId) {
		try {
			Permission exists = permissionService.findByClientIdAndCode(SaasContext.getCurrentClienId(), t.getCode());
			List<Permission> children = t.getChildren();
			if (exists == null) {
				if (t.getEnabled() == null) {
					t.setEnabled(YesNoType.YES.getValue());
				}
				t.setOrder(++order);
				t.setClientId(clientId);
				permissionService.insertSelective(t);
			} else {
				exists.setEnabled(t.getEnabled() == null ? YesNoType.YES.getValue() : t.getEnabled());
				exists.setCode(t.getCode());
				exists.setName(t.getName());
				exists.setType(t.getType());
				exists.setUrl(t.getUrl());
				exists.setIcon(t.getIcon());
				exists.setRoute(t.getRoute());
				exists.setOrder(++order);
				exists.setClientId(clientId);
				permissionService.updateByPrimaryKeySelective(exists);
			}
			if (ICollections.hasElements(children)) {
				children.forEach(child -> {
					if (exists == null) {
						child.setParentId(t.getId());
					} else {
						child.setParentId(exists.getId());
					}
					savePerm(child, clientId);
				});
			}
		} catch (Exception e) {
			log.warn("save permission info:{} error, cause by:{}", JSON.toJSONString(t), e.getMessage());
		}
	}
}
