package com.info.baymax.dsp.gateway.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;

import java.net.URLEncoder;
import java.util.Map;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.dsp.auth.api.utils.SecurityUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessHeaderFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FORM_BODY_WRAPPER_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.set("startTime", System.currentTimeMillis());

        Authentication authentication = SecurityUtils.getCurrentAuthentication();
        if (authentication == null) {
            return null;
        }

        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }
        OAuth2Authentication oauth = (OAuth2Authentication) authentication;
        Map<String, Object> details = (Map<String, Object>) oauth.getUserAuthentication().getDetails();
        if (details == null) {
            return null;
        }

        Map<String, Object> userAuthenticationMap = (Map<String, Object>) details.get("userAuthentication");
        if (userAuthenticationMap == null) {
            return null;
        }
        try {
            JSONObject json = JSON.parseObject(JSON.toJSONString(userAuthenticationMap));
            SaasContext saasContext = SaasContext.getCurrentSaasContext();
            saasContext.setClientId(json.getString("clientId"));
            saasContext.setTenantName(json.getString("tenant"));
            saasContext.setTenantId(json.getLong("tenantId"));
            saasContext.setUserId(json.getLong("userId"));
            saasContext.setUsername(json.getString("name"));
            saasContext.setAdmin(json.getBooleanValue("admin"));
            requestContext.addZuulRequestHeader(SaasContext.SAAS_CONTEXT_KEY,
                URLEncoder.encode(JSON.toJSONString(saasContext), "UTF-8"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
