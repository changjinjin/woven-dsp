/*
 * Copyright 2011-2016 the original author or authors.
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
package com.info.baymax.security.cas.reactive.web.authentication;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.util.Assert;

import java.net.MalformedURLException;
import java.util.regex.Pattern;

/**
 * The {@code AuthenticationDetailsSource} that is set on the {@code CasAuthenticationFilter} should return a value that
 * implements {@code ServiceAuthenticationDetails} if the application needs to authenticate dynamic service urls. The
 * {@code ServiceAuthenticationDetailsSource#buildDetails(HttpServletRequest)} creates a default
 * {@code ServiceAuthenticationDetails}.
 *
 * @author Rob Winch
 */
public class ServerServiceAuthenticationDetailsSource {
    // ~ Instance fields
    // ================================================================================================

    private final Pattern artifactPattern;

    private ServiceProperties serviceProperties;

    // ~ Constructors
    // ===================================================================================================

    /**
     * Creates an implementation that uses the specified ServiceProperites and the default CAS artifactParameterName.
     *
     * @param serviceProperties The ServiceProperties to use to construct the serviceUrl.
     */
    public ServerServiceAuthenticationDetailsSource(ServiceProperties serviceProperties) {
        this(serviceProperties, ServiceProperties.DEFAULT_CAS_ARTIFACT_PARAMETER);
    }

    /**
     * Creates an implementation that uses the specified artifactParameterName
     *
     * @param serviceProperties     The ServiceProperties to use to construct the serviceUrl.
     * @param artifactParameterName the artifactParameterName that is removed from the current URL. The result becomes
     *                              the service url. Cannot be null and cannot be an empty String.
     */
    public ServerServiceAuthenticationDetailsSource(ServiceProperties serviceProperties, String artifactParameterName) {
        Assert.notNull(serviceProperties, "serviceProperties cannot be null");
        this.serviceProperties = serviceProperties;
        this.artifactPattern = DefaultServerServiceAuthenticationDetails.createArtifactPattern(artifactParameterName);
    }

    // ~ Methods
    // ========================================================================================================

    /**
     * @param context the {@code HttpServletRequest} object.
     * @return the {@code ServiceAuthenticationDetails} containing information about the current request
     */
    public ServerServiceAuthenticationDetails buildDetails(String remoteAddress, String sessionId,
                                                           ServerHttpRequest request) {
        try {
            return new DefaultServerServiceAuthenticationDetails(remoteAddress, sessionId, request,
                serviceProperties.getService(), artifactPattern);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}