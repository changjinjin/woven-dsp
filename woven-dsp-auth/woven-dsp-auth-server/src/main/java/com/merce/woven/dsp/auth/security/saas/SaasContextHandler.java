//package com.merce.woven.dsp.auth.security.saas;
//
//import java.nio.charset.Charset;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//
//import com.merce.woven.common.message.result.ErrType;
//import com.merce.woven.common.message.result.Response;
//import com.merce.woven.common.utils.ICollections;
//import com.merce.woven.dsp.auth.security.support.authentication.login.LoginAuthenticationToken;
//import com.merce.woven.dsp.auth.security.support.token.tenant.TenantDetails;
//import com.merce.woven.dsp.data.sys.entity.security.Tenant;
//import com.merce.woven.dsp.data.sys.entity.security.User;
//import com.merce.woven.dsp.data.sys.service.security.TenantService;
//import com.merce.woven.dsp.data.sys.service.security.UserService;
//
//import reactor.core.publisher.Mono;
//
///**
// * 处理当前租户和登录用户信息到上下文中
// *
// * @author jingwei.yang
// * @date 2019年5月15日 下午2:40:24
// */
//@Component
//public class SaasContextHandler {
//
//	private static Logger logger = LoggerFactory.getLogger(SaasContextHandler.class);
//
//	@Autowired
//	private TenantService tenantService;
//
//	@Autowired
//	private UserService merceUserService;
//
//	public void handle(HttpServletRequest request, LoginAuthenticationToken authentication) {
//
//		// 跳过非/api开头的请求
//		String path = request.getRequestURI();
//		if (!path.startsWith("/api/")) {
//			return;
//		}
//
//		SaasContext ctx = SaasContext.getCurrentSaasContext();
//
//		// 处理请求主机信息
//		String host = request.getHeader("Host");
//		if (StringUtils.isNotEmpty(host)) {
//			host = host.split(":")[0];
//		}
//
//		List<String> ips = request.getHeader("X-Forwarded-For");
//		String clientIp = null;
//		if (ICollections.hasElements(ips)) {
//			clientIp = ips.get(0);
//		} else {
//			clientIp = request.getRemoteAddress().getHostName();
//		}
//		ctx.setHost(host);
//		ctx.setClientIp(clientIp);
//
//		// 处理租户信息和用户信息
//		if (authentication != null) {
//			TenantDetails clientDetails = authentication.getClientDetails();
//			String clientId = clientDetails.getClientId();
//
//			// 设置租户信息
//			Tenant tenant = tenantService.findByName(clientId);
//			if (tenant == null) {
//				response.setStatusCode(HttpStatus.OK);
//				response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
//				DataBuffer buffer = response.bufferFactory()
//						.wrap(JSON.toJSONString(Response.ok(ErrType.UNAUTHORIZED)).getBytes(Charset.defaultCharset()));
//				response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
//			}
//
//			if (logger.isTraceEnabled()) {
//				logger.trace("{} : set tenant {} ", request.getURI(), JSON.toJSONString(tenant));
//			}
//
//			// 设置用户信息
//			User user = merceUserService.findByTenantAndLoginId(tenant.getId(), authentication.getName());
//			if (user == null) {
//				response.setStatusCode(HttpStatus.OK);
//				response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
//				DataBuffer buffer = response.bufferFactory()
//						.wrap(JSON.toJSONString(Response.ok(ErrType.UNAUTHORIZED)).getBytes(Charset.defaultCharset()));
//				response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
//			}
//			if (logger.isTraceEnabled()) {
//				logger.trace("{} : set user {} ", request.getURI(), JSON.toJSONString(user));
//			}
//
//			ctx.setTenant(tenant);
//			ctx.setUser(user);
//		}
//
//	}
//
//}
