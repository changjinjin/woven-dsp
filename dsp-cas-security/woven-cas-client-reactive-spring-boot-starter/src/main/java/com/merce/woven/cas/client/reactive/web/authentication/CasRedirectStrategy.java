package com.merce.woven.cas.client.reactive.web.authentication;

import cn.hutool.core.util.StrUtil;
import com.merce.woven.cas.client.reactive.util.CASAuthUtil;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Setter
public class CasRedirectStrategy extends DefaultServerRedirectStrategy {
    private HttpStatus httpStatus = HttpStatus.FOUND;
    private boolean contextRelative = true;
    private String indexUrl;

    @Override
    public Mono<Void> sendRedirect(ServerWebExchange exchange, URI location) {
        ServerHttpResponse response = exchange.getResponse();
        final boolean isAuthenticated = CASAuthUtil.hasAuthenticatedCookie(exchange, true);
        if (StrUtil.isNotBlank(indexUrl) && !isAuthenticated) {
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().add("Location", indexUrl);
            return Mono.empty();
        }

        String redirectURL = CASAuthUtil.getRedirectURL(exchange);
        if (redirectURL != null && redirectURL.length() > 0 && !isAuthenticated) {
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().add("Location", redirectURL);
        } else {
            response.setStatusCode(httpStatus);
            response.getHeaders().setLocation(createLocation(exchange, location));
        }
        return Mono.empty();
    }

    private URI createLocation(ServerWebExchange exchange, URI location) {
        if (!this.contextRelative) {
            return location;
        }
        String url = location.toString();
        if (url.startsWith("/")) {
            String context = exchange.getRequest().getPath().contextPath().value();
            return URI.create(context + url);
        }
        return location;
    }
}
