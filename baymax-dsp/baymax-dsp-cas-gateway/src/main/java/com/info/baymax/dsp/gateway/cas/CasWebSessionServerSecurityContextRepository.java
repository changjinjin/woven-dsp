package com.info.baymax.dsp.gateway.cas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
/**
 * @Author: haijun
 * @Date: 2020/10/13 19:02
 */
@Primary
@Component("serverSecurityContextRepository")
public class CasWebSessionServerSecurityContextRepository implements ServerSecurityContextRepository {
    @Autowired
    private CasSaasContextHandler saasContextHandler;

    /**
     * The default session attribute name to save and load the {@link org.springframework.security.core.context.SecurityContext}
     */
    public static final String DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME = "SPRING_SECURITY_CONTEXT";

    private String springSecurityContextAttrName = DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME;

    /**
     * Sets the session attribute name used to save and load the {@link org.springframework.security.core.context.SecurityContext}
     * @param springSecurityContextAttrName the session attribute name to use to save and
     * load the {@link org.springframework.security.core.context.SecurityContext}
     */
    public void setSpringSecurityContextAttrName(String springSecurityContextAttrName) {
        Assert.hasText(springSecurityContextAttrName, "springSecurityContextAttrName cannot be null or empty");
        this.springSecurityContextAttrName = springSecurityContextAttrName;
    }

    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return exchange.getSession()
                .doOnNext(session -> {
                    if (context == null) {
                        session.getAttributes().remove(this.springSecurityContextAttrName);
                    } else {
                        session.getAttributes().put(this.springSecurityContextAttrName, context);
                    }
                })
                .flatMap(session -> session.changeSessionId());
    }

    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return exchange.getSession()
                .map(WebSession::getAttributes)
                .flatMap( attrs -> {
                    SecurityContext context = (SecurityContext) attrs.get(this.springSecurityContextAttrName);
                    if(context != null && context.getAuthentication() instanceof CasAuthenticationToken) {
                        saasContextHandler.handle(exchange, (CasAuthenticationToken)context.getAuthentication());
                    }
                    return Mono.justOrEmpty(context);
                });
    }
}
