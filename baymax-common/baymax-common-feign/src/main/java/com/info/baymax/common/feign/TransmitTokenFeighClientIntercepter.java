package com.info.baymax.common.feign;

import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.utils.JsonUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 服务之间feign调用是需要传递当前用户信息，通过feign RequestInterceptor实现
 *
 * @author jingwei.yang
 * @date 2020年1月4日 下午2:47:31
 */
public class TransmitTokenFeighClientIntercepter implements RequestInterceptor {

    @Autowired
    @Nullable
    private RequestTemplateApplier requestTemplateApplier;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (requestTemplateApplier == null) { 
            requestTemplateApplier = new DefaultRequestTemplateApplier();
        }
        requestTemplateApplier.apply(requestTemplate);
    }

    public static final class DefaultRequestTemplateApplier implements RequestTemplateApplier {
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
                    URLEncoder.encode(JsonUtils.toJson(SaasContext.getCurrentSaasContext()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}