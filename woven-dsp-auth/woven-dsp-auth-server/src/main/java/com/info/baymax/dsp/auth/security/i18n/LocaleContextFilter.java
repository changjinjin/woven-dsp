//package com.info.baymax.dsp.auth.security.i18n;
//
//import java.util.Locale;
//
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//
//import reactor.core.publisher.Mono;
//
//@Component
//@Order(Integer.MIN_VALUE + 1)
//public class LocaleContextFilter implements WebFilter {
//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//		LocaleContextHolder.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
//		LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE, true);
//		return chain.filter(exchange);
//	}
//}
