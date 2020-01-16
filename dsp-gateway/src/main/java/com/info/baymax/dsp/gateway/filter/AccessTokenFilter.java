package com.info.baymax.dsp.gateway.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;

import java.net.URLEncoder;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.dsp.auth.api.utils.SecurityUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessTokenFilter extends ZuulFilter {
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

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.set("startTime", System.currentTimeMillis());
        try {
            SaasContext saasContext = SecurityUtils.getSaasContext();
            requestContext.addZuulRequestHeader(SaasContext.SAAS_CONTEXT_KEY,
                URLEncoder.encode(JSON.toJSONString(saasContext), "UTF-8"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
