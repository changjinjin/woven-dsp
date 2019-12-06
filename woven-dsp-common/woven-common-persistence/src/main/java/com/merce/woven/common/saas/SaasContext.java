package com.merce.woven.common.saas;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SaasContext {

	private String host;
	private String clientIp;

	private String clientId;
	private Long tenantId;
	private String tenantName;

	private Long userId;
	private String username;
	private boolean admin;

	private static ThreadLocal<SaasContext> saasContextThreadLocal = new ThreadLocal<>();

	public static SaasContext getCurrentSaasContext() {
		SaasContext ctx = saasContextThreadLocal.get();
		if (ctx == null) {
			saasContextThreadLocal.set(ctx = new SaasContext());
		}
		return ctx;
	}

	public static String getCurrentClienId() {
		return getCurrentSaasContext().getClientId();
	}

	public static Long getCurrentTenantId() {
		return getCurrentSaasContext().getTenantId();
	}

	public static Long getCurrentTenantName() {
		return getCurrentSaasContext().getTenantId();
	}

	public static Long getCurrentUserId() {
		return getCurrentSaasContext().getUserId();
	}

	public static String getCurrentUsername() {
		return getCurrentSaasContext().getUsername();
	}

	public static boolean getCurrentUserIsAdmin() {
		return getCurrentSaasContext().isAdmin();
	}

	public static Map<String, Object> current() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tenantId", getCurrentTenantId());
		map.put("owner", getCurrentUserId());
		map.put("admin", getCurrentUserIsAdmin());
		return map;
	}

	public static void clear() {
		SaasContext context = getCurrentSaasContext();
		context.setClientIp(null);
		context.setHost(null);
		context.setClientId(null);
		context.setClientIp(null);
		context.setTenantId(null);
		context.setTenantName(null);
		context.setUserId(null);
		context.setUsername(null);
	}
}
