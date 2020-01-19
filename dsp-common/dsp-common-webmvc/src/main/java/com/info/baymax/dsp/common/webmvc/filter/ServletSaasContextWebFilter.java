package com.info.baymax.dsp.common.webmvc.filter;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.saas.SaasContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * SaasContext 拦截初始化
 *
 * @author jingwei.yang
 * @date 2019年12月17日 上午10:37:51
 */
@Slf4j
@Component
public class ServletSaasContextWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
        throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String saasContextHeader = request.getHeader(SaasContext.SAAS_CONTEXT_KEY);
        if (StringUtils.isNotEmpty(saasContextHeader)) {
            String decode = null;
            try {
                decode = URLDecoder.decode(saasContextHeader, "UTF-8");
                SaasContext saasContext = JSON.parseObject(decode, SaasContext.class);
                if (saasContext != null) {
                    SaasContext.setCurrentSaasContext(saasContext);
                }
                log.debug("fix current SaasContext :{}", decode);
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
        chain.doFilter(request, response);
    }
}
