package com.info.baymax.security.cas.reactive.client.web.authentication;

import cn.hutool.core.util.StrUtil;
import com.info.baymax.security.cas.reactive.client.config.CasServiceProperties;
import com.info.baymax.security.cas.reactive.client.util.CASAuthUtil;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

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
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        logger.info("path: {}, URI: {}", request.getPath(), request.getURI());
        //新cas中将跳转uri放在service path里
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        if(queryParams.containsKey("ticket") && queryParams.containsKey("href")){
            ///cas/login?ticket=ST-68-p-ZDkC7TbkPBRtskN70l8KlN4iohadoop-04&href=info-dashboard_jobSchedule_scheduleList
            String href = queryParams.getFirst("href");
            indexUrl  = casServiceProperties.getBaseUrl();//只取base url
            if(!indexUrl.endsWith("/")){
                indexUrl = indexUrl + "/";
            }
            if(href.startsWith("/")){
                href = href.substring(1);
            }
            indexUrl = indexUrl + href.replace("_", "/");
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().add("href", indexUrl); //跳转到资源页
            logger.info("cas login redirect new href: {}, Location: {}", href, indexUrl);
            return Mono.empty();
        }

        String href = null;//Cookie: href=/resourceManProject
        String uri = null;//Cookie: uri=old/interactiveQuery
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        List<HttpCookie> hrefList = cookies.get("href");//旧的逻辑里是href拼接
        List<HttpCookie> uriList = cookies.get("uri");//新的逻辑里是uri
        if(hrefList != null && hrefList.size() > 0){
            href = hrefList.get(0).getValue();
            indexUrl  = casServiceProperties.getBaseUrl();//只取base url
            if(!indexUrl.endsWith("/")){
                indexUrl = indexUrl + "/";
            }
            indexUrl = indexUrl +"#"+ href;
            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().add("href", indexUrl); //跳转到资源页
            logger.info("cas login redirect href: {}, Location: {}", href, indexUrl);
            return Mono.empty();
        }else if(uriList != null && uriList.size() > 0){
            uri = uriList.get(0).getValue();
            indexUrl  = casServiceProperties.getBaseUrl();//只取base url
            if(!indexUrl.endsWith("/")){
                indexUrl = indexUrl + "/";
            }
            if(uri.startsWith("/")){
                uri = uri.substring(1);
            }
            indexUrl = indexUrl + uri;
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().add("Location", indexUrl); //跳转到资源页
            logger.info("cas login redirect uri: {}, Location: {}", uri, indexUrl);
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
