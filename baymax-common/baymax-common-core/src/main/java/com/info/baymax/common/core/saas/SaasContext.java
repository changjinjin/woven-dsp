package com.info.baymax.common.core.saas;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SaasContext implements Serializable {
    private static final long serialVersionUID = 7238846400682679965L;
    public static final String SAAS_CONTEXT_KEY = "SaasContext";

    private String host;

    private String clientId;
    private String tenantId;
    private String tenantName;

    private String userId;
    private String username;
    private boolean admin;
    private String userType;

    private static ThreadLocal<SaasContext> saasContextThreadLocal = new ThreadLocal<>();

    public static SaasContext getCurrentSaasContext() {
        SaasContext ctx = saasContextThreadLocal.get();
        if (ctx == null) {
            saasContextThreadLocal.set(ctx = new SaasContext());
        }
        return ctx;
    }

    public static void setCurrentSaasContext(SaasContext saasContext) {
        saasContextThreadLocal.set(saasContext);
    }

    public static String getCurrentClienId() {
        return getCurrentSaasContext().getClientId();
    }

    public static String getCurrentTenantId() {
        return getCurrentSaasContext().getTenantId();
    }

    public static String getCurrentTenantName() {
        return getCurrentSaasContext().getTenantName();
    }

    public static String getCurrentUserId() {
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
        context.setHost(null);
        context.setClientId(null);
        context.setTenantId(null);
        context.setTenantName(null);
        context.setUserId(null);
        context.setUsername(null);
    }

    public static void initSaasContext(String tenantId, String userId) {
        SaasContext ctx = getCurrentSaasContext();

        ctx.setTenantId(tenantId);
        ctx.setUserId(userId);
    }
}
