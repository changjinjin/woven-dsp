/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
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

package org.springframework.security.cas.reactive.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.reactive.utils.CommonUtils;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

/**
 * Used by the <code>ExceptionTranslationFilter</code> to commence authentication via the JA-SIG Central Authentication
 * Service (CAS).
 * <p>
 * The user's browser will be redirected to the JA-SIG CAS enterprise-wide login page. This page is specified by the
 * <code>loginUrl</code> property. Once login is complete, the CAS login page will redirect to the page indicated by the
 * <code>service</code> property. The <code>service</code> is a HTTP URL belonging to the current application. The
 * <code>service</code> URL is monitored by the {@link CasAuthenticationFilter}, which will validate the CAS login was
 * successful.
 *
 * @author Ben Alex
 * @author Scott Battaglia
 */
@Setter
@Getter
public class CasServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint, InitializingBean {
    // ~ Instance fields
    // ================================================================================================
    private ServiceProperties serviceProperties;

    private String loginUrl;

    /**
     * Determines whether the Service URL should include the session id for the specific user. As of CAS 3.0.5, the
     * session id will automatically be stripped. However, older versions of CAS (i.e. CAS 2), do not automatically
     * strip the session identifier (this is a bug on the part of the older server implementations), so an option to
     * disable the session encoding is provided for backwards compatibility.
     * <p>
     * By default, encoding is enabled.
     */
    private boolean encodeServiceUrlWithSessionId = true;

    // ~ Methods
    // ========================================================================================================

    public void afterPropertiesSet() {
        Assert.hasLength(this.loginUrl, "loginUrl must be specified");
        Assert.notNull(this.serviceProperties, "serviceProperties must be specified");
        Assert.notNull(this.serviceProperties.getService(), "serviceProperties.getService() cannot be null.");
    }

    public Mono<Void> commence(ServerWebExchange exchange, final AuthenticationException authenticationException) {
        final String urlEncodedService = createServiceUrl(exchange);
        final String redirectUrl = createRedirectUrl(urlEncodedService);
        preCommence(exchange);
        CommonUtils.sendRedirect(exchange.getResponse(), redirectUrl);
        return Mono.<Void>empty();
    }

    /**
     * Constructs a new Service Url. The default implementation relies on the CAS client to do the bulk of the work.
     *
     * @return the constructed service url. CANNOT be NULL.
     */
    protected String createServiceUrl(ServerWebExchange exchange) {
        return CommonUtils.constructServiceUrl(exchange, this.serviceProperties.getService(), null,
            this.serviceProperties.getArtifactParameter(), this.encodeServiceUrlWithSessionId);
    }

    /**
     * Constructs the Url for Redirection to the CAS server. Default implementation relies on the CAS client to do the
     * bulk of the work.
     *
     * @param serviceUrl the service url that should be included.
     * @return the redirect url. CANNOT be NULL.
     */
    protected String createRedirectUrl(final String serviceUrl) {
        return CommonUtils.constructRedirectUrl(this.loginUrl, this.serviceProperties.getServiceParameter(), serviceUrl,
            this.serviceProperties.isSendRenew(), false);
    }

    /**
     * Template method for you to do your own pre-processing before the redirect occurs.
     *
     * @param exchange  the ServerWebExchange
     */
    protected void preCommence(ServerWebExchange exchange) {
    }
}
