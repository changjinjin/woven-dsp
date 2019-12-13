package com.info.baymax.dsp.gateway.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.netflix.zuul.ZuulFilter;

public class ErrorFilter extends ZuulFilter {

	@Override
	public Object run() {
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return FilterConstants.ERROR_TYPE;
	}

}
