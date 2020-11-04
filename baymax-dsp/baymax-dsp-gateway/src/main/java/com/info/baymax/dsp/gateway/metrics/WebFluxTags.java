package com.info.baymax.dsp.gateway.metrics;
/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

import com.alibaba.fastjson.JSON;
import com.info.baymax.dsp.gateway.web.method.RequestUriMappingsHolder;
import com.info.baymax.dsp.gateway.web.method.RestOperation;

import io.micrometer.core.instrument.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Factory methods for {@link Tag Tags} associated with a request-response exchange that is handled by WebFlux. rewrite
 * WebFluxTags for gateway tag uri shoud't be UNKNOWN create by pengchuan.chen on 2019/6/19
 */
public final class WebFluxTags {

	private static final Tag URI_NOT_FOUND = Tag.of("uri", "NOT_FOUND");

	private static final Tag URI_REDIRECTION = Tag.of("uri", "REDIRECTION");

	private static final Tag URI_ROOT = Tag.of("uri", "root");

	// private static final Tag URI_UNKNOWN = Tag.of("uri", "UNKNOWN");

	private static final Tag EXCEPTION_NONE = Tag.of("exception", "None");

	private static final Tag OUTCOME_UNKNOWN = Tag.of("outcome", "UNKNOWN");

	private static final Tag OUTCOME_INFORMATIONAL = Tag.of("outcome", "INFORMATIONAL");

	private static final Tag OUTCOME_SUCCESS = Tag.of("outcome", "SUCCESS");

	private static final Tag OUTCOME_REDIRECTION = Tag.of("outcome", "REDIRECTION");

	private static final Tag OUTCOME_CLIENT_ERROR = Tag.of("outcome", "CLIENT_ERROR");

	private static final Tag OUTCOME_SERVER_ERROR = Tag.of("outcome", "SERVER_ERROR");

	private WebFluxTags() {
	}

	/**
	 * Creates a {@code method} tag based on the
	 * {@link org.springframework.http.server.reactive.ServerHttpRequest#getMethod() method} of the
	 * {@link ServerWebExchange#getRequest()} request of the given {@code exchange}.
	 *
	 * @param exchange the exchange
	 * @return the method tag whose value is a capitalized method (e.g. GET).
	 */
	public static Tag method(ServerWebExchange exchange) {
		return Tag.of("method", exchange.getRequest().getMethodValue());
	}

	/**
	 * Creates a {@code status} tag based on the response status of the given {@code exchange}.
	 *
	 * @param exchange the exchange
	 * @return the status tag derived from the response status
	 */
	public static Tag status(ServerWebExchange exchange) {
		HttpStatus status = exchange.getResponse().getStatusCode();
		if (status == null) {
			status = HttpStatus.OK;
		}
		return Tag.of("status", String.valueOf(status.value()));
	}

	/**
	 * Creates a {@code uri} tag based on the URI of the given {@code exchange}. Uses the
	 * {@link HandlerMapping#BEST_MATCHING_PATTERN_ATTRIBUTE} best matching pattern.
	 *
	 * @param exchange the exchange
	 * @return the uri tag derived from the exchange
	 */
	public static Tag uri(ServerWebExchange exchange) {
		PathPattern pathPattern = exchange.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		if (pathPattern != null) {
			return Tag.of("uri", pathPattern.getPatternString());
		}
		HttpStatus status = exchange.getResponse().getStatusCode();
		if (status != null) {
			if (status.is3xxRedirection()) {
				return URI_REDIRECTION;
			}
			if (status == HttpStatus.NOT_FOUND) {
				return URI_NOT_FOUND;
			}
		}
		// String path = getPathInfo(exchange);
		String path = exchange.getRequest().getPath().value();
		if (path.isEmpty()) {
			return URI_ROOT;
		}
		return Tag.of("uri", path);
		// return URI_UNKNOWN;
	}

	public static Tag operation(ServerWebExchange exchange, RequestUriMappingsHolder holder) {
		RestOperation operation = holder.getOperation(exchange);
		return Tag.of("operation", operation != null ? operation.operation() : exchange.getRequest().getPath().value());
	}

	/**
	 * Creates an {@code exception} tag based on the {@link Class#getSimpleName() simple name} of the class of the given
	 * {@code exception}.
	 *
	 * @param exception the exception, may be {@code null}
	 * @return the exception tag derived from the exception
	 */
	public static Tag exception(Throwable exception) {
		if (exception != null) {
			String simpleName = exception.getClass().getSimpleName();
			return Tag.of("exception", StringUtils.hasText(simpleName) ? simpleName : exception.getClass().getName());
		}
		return EXCEPTION_NONE;
	}

	/**
	 * Creates an {@code outcome} tag based on the response status of the given {@code exchange}.
	 *
	 * @param exchange the exchange
	 * @return the outcome tag derived from the response status
	 * @since 2.1.0
	 */
	public static Tag outcome(ServerWebExchange exchange) {
		HttpStatus status = exchange.getResponse().getStatusCode();
		if (status != null) {
			if (status.is1xxInformational()) {
				return OUTCOME_INFORMATIONAL;
			}
			if (status.is2xxSuccessful()) {
				return OUTCOME_SUCCESS;
			}
			if (status.is3xxRedirection()) {
				return OUTCOME_REDIRECTION;
			}
			if (status.is4xxClientError()) {
				return OUTCOME_CLIENT_ERROR;
			}
			return OUTCOME_SERVER_ERROR;
		}
		return OUTCOME_UNKNOWN;
	}

	public static Tag params(ServerWebExchange exchange) {
		return Tag.of("params", getParams(exchange));
	}

	public static String getParams(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		HttpMethod httpMethod = request.getMethod();
		String queryString = "";
		switch (httpMethod) {
		case POST:
			queryString = (String) exchange.getAttributes()
					.getOrDefault(HttpCachedBodyGlobalFilter.CACHED_REQUEST_BODY_KEY, "");
			break;
		default:
			MultiValueMap<String, String> queryParams = request.getQueryParams();
			if (queryParams != null) {
				queryString = JSON.toJSONString(queryParams);
			}
			break;
		}
		return queryString;
	}

	public static Tag clientIp(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();

		String clientIp = null;

		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			List<String> list = headers.get("x-forwarded-for");
			if (list != null) {
				clientIp = list.toString();
			}
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			List<String> list = headers.get("Proxy-Client-IP");
			if (list != null) {
				clientIp = list.toString();
			}
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			List<String> list = headers.get("WL-Proxy-Client-IP");
			if (list != null) {
				clientIp = list.toString();
			}
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			List<String> list = headers.get("WL-Proxy-Client-IP");
			if (list != null) {
				clientIp = list.toString();
			}
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			InetSocketAddress remoteAddress = request.getRemoteAddress();
			if (remoteAddress != null) {
				clientIp = remoteAddress.getAddress().getHostAddress();
			}
		}
		if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
			List<String> list = headers.get("Host");
			if (list != null) {
				clientIp = list.toString();
			}
		}

		return Tag.of("clientIp", clientIp == null ? "" : clientIp);
	}
}