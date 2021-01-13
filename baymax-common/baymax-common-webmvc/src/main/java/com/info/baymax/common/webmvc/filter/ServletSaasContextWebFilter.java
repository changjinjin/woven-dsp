package com.info.baymax.common.webmvc.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

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
				SaasContext saasContext = JsonUtils.fromJson(decode, SaasContext.class);
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
