package com.info.baymax.security.cas.reactive.client.web.authentication;

import cn.hutool.core.util.StrUtil;

import com.info.baymax.security.cas.reactive.client.config.CasServiceProperties;
import com.info.baymax.security.cas.reactive.client.util.CASAuthUtil;

import java.util.List;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Setter
@ComponentScan
public class CasRedirectStrategy extends DefaultServerRedirectStrategy {
    private Logger logger = LoggerFactory.getLogger(CasRedirectStrategy.class);
    private HttpStatus httpStatus = HttpStatus.FOUND;
    private boolean contextRelative = true;
    private String indexUrl;

    @Autowired
    private CasServiceProperties casServiceProperties;


    @Override
    public Mono<Void> sendRedirect(ServerWebExchange exchange, URI location) {
        ServerHttpResponse response = exchange.getResponse();
        logger.info("path: {}, URI: {}", exchange.getRequest().getPath(), exchange.getRequest().getURI());
        String href = null;//Cookie: href=/resourceManProject

        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        List<HttpCookie> list = cookies.get("href");
        if(list != null && list.size() > 0){
            href = list.get(0).getValue();
        }

        if(href != null){
            indexUrl  = casServiceProperties.getBaseUrl();//只取base url
            if(!indexUrl.endsWith("/")){
                indexUrl = indexUrl + "/";
            }
            indexUrl = indexUrl +"#"+ href;
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().add("Location", indexUrl); //跳转到资源页
            logger.info("cas login redirect href: {}, Location: {}", href, indexUrl);
            return Mono.empty();
        }

        final boolean isAuthenticated = CASAuthUtil.hasAuthenticatedCookie(exchange, true);
        if (StrUtil.isNotBlank(indexUrl) && !isAuthenticated) {
            indexUrl  = casServiceProperties.getBaseUrl();//只取base url
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().add("Location", indexUrl); //跳转到资源页
            logger.info("cas login index location, href: {}, Location: {}", href, indexUrl);
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
