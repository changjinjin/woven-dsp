package com.info.baymax.common.comp.feign;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.saas.SaasContext;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 服务之间feign调用是需要传递当前用户信息，通过feign RequestInterceptor实现
 *
 * @author jingwei.yang
 * @date 2020年1月4日 下午2:47:31
 */
public class TransmitTokenFeighClientIntercepter implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 调用Baymax时候需要以下信息
        if (SaasContext.getCurrentUserId() == null) {
            requestTemplate.header("temp", "true");
            return;
        }
        requestTemplate.header("userId", SaasContext.getCurrentUserId());
        requestTemplate.header("loginId", SaasContext.getCurrentUsername());
        requestTemplate.header("tenantName", SaasContext.getCurrentTenantName());
        requestTemplate.header("tenantId", SaasContext.getCurrentTenantId());

        // 调用dsp是需要以下信息
        try {
            requestTemplate.header(SaasContext.SAAS_CONTEXT_KEY,
                URLEncoder.encode(JSON.toJSONString(SaasContext.getCurrentSaasContext()), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}