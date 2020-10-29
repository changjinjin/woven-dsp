package com.info.baymax.security.cas.reactive.client.util;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CASAuthUtil {
    public static final String AUTHENTICATED_COOKIE_NAME = "authenticated";
    public static final String AUTHENTICATED_COOKIE_VALUE = "1";
    public static final String REDIRECT_URL_SESSION_ATTRIBUTE = "REAL_REDIRECT_URL";
    public static final String AJAX_REQUEST_HEADER = "XMLHttpRequest";

    public static String getRedirectURL(ServerWebExchange exchange) {
        Map<String, Object> attributes = exchange.getAttributes();
        Object object = attributes.get("REAL_REDIRECT_URL");
        if (object != null) {
            attributes.remove("REAL_REDIRECT_URL");
            return object.toString();
        }
        return null;
    }

    public static boolean isAjaxRequest(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        final String headerRequestedWith = headers.getFirst(AJAX_REQUEST_HEADER);
        return "XMLHttpRequest".equalsIgnoreCase(headerRequestedWith);
    }

    public static String buildUnAuthenticatedJson(ServerWebExchange exchange, final String redirectUrl) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        final String referer = headers.getFirst("Referer");
        exchange.getAttributes().put("REAL_REDIRECT_URL", referer);
        final String origin = headers.getFirst("Origin");
        if (origin != null) {
            response.getHeaders().add("Access-Control-Allow-Origin", origin);
        }
        return "{\"status\":403,\"redirectURL\":\"" + redirectUrl + "\"}";
    }

    public static boolean hasAuthenticatedCookie(ServerWebExchange exchange, final boolean createIfNotExist) {
        boolean authenticated = false;
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        List<HttpCookie> list = cookies.get("authenticated");
        if (list != null && !list.isEmpty()) {
            authenticated = true;
        }
        if (!authenticated && createIfNotExist) {
            exchange.getResponse().addCookie(
                ResponseCookie.from("authenticated", "1").maxAge(Duration.ofSeconds(1700)).path("/").build());
        }
        return authenticated;
    }

    public static void removeAuthenticatedCookie(ServerHttpResponse response) {
        response.addCookie(ResponseCookie.from("authenticated", "").maxAge(Duration.ZERO).path("/").build());
        response.addCookie(ResponseCookie.from("X-SESSION-ID", "").maxAge(Duration.ZERO).path("/").build());
    }

    public static String getServerBaseURL(ServerHttpRequest request) {
        URI uri = request.getURI();
        String path = uri.getPath();
        String uriStr = uri.toString();
        String query = uri.getQuery();
        String serverBaseURL = uriStr.substring(0,
            uriStr.length() - (query == null ? 0 : query.length()) - path.length());
        if (serverBaseURL.endsWith("/")) {
            serverBaseURL = serverBaseURL.substring(0, serverBaseURL.length() - 1);
        }
        return serverBaseURL;
    }
}
