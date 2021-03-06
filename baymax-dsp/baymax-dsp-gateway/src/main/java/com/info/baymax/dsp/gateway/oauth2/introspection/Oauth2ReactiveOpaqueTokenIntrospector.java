
/*
 * Copyright 2002-2019 the original author or authors.
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
package com.info.baymax.dsp.gateway.oauth2.introspection;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.Audience;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.*;

/**
 * A Nimbus implementation of {@link ReactiveOpaqueTokenIntrospector} that verifies and introspects a token using the
 * configured <a href="https://tools.ietf.org/html/rfc7662" target="_blank">OAuth 2.0 Introspection Endpoint</a>.
 *
 * @author Josh Cummings
 * @since 5.2
 */
public class Oauth2ReactiveOpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {
    private URI introspectionUri;
    private WebClient webClient;

    /**
     * Creates a {@code OpaqueTokenReactiveAuthenticationManager} with the provided parameters
     *
     * @param introspectionUri The introspection endpoint uri
     */
    public Oauth2ReactiveOpaqueTokenIntrospector(String introspectionUri) {
        Assert.hasText(introspectionUri, "introspectionUri cannot be empty");
        this.introspectionUri = URI.create(introspectionUri);
        this.webClient = WebClient.builder().build();
    }

    /**
     * Creates a {@code OpaqueTokenReactiveAuthenticationManager} with the provided parameters
     *
     * @param introspectionUri The introspection endpoint uri
     * @param webClient        The client for performing the introspection request
     */
    public Oauth2ReactiveOpaqueTokenIntrospector(String introspectionUri, WebClient webClient) {
        Assert.hasText(introspectionUri, "introspectionUri cannot be null");
        Assert.notNull(webClient, "webClient cannot be null");

        this.introspectionUri = URI.create(introspectionUri);
        this.webClient = webClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        // @formatter:off
        return Mono.just(token)
            .flatMap(this::makeRequest)
            .flatMap(this::adaptToNimbusResponse)
            .map(this::parseNimbusResponse)
            .map(this::castToNimbusSuccess)
            .doOnNext(response -> validate(token, response))
            .map(this::convertClaimsSet)
            .onErrorMap(e -> !(e instanceof OAuth2IntrospectionException), this::onError);
        // @formatter:on
    }

    private Mono<ClientResponse> makeRequest(String token) {
        // @formatter:off
        return this.webClient
            .get()
            .uri(this.introspectionUri)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, OAuth2AccessToken.BEARER_TYPE + " " + token)
            .exchange();
        // @formatter:on
    }

    private Mono<HTTPResponse> adaptToNimbusResponse(ClientResponse responseEntity) {
        HTTPResponse response = new HTTPResponse(responseEntity.rawStatusCode());
        response.setHeader(HttpHeaders.CONTENT_TYPE, responseEntity.headers().contentType().get().toString());
        if (response.getStatusCode() != HTTPResponse.SC_OK) {
            return responseEntity.bodyToFlux(DataBuffer.class).map(DataBufferUtils::release)
                .then(Mono.error(new OAuth2IntrospectionException(
                    "Introspection endpoint responded with " + response.getStatusCode())));
        }
        return responseEntity.bodyToMono(String.class).doOnNext(response::setContent).map(body -> response);
    }

    private Oauth2TokenIntrospectionResponse parseNimbusResponse(HTTPResponse response) {
        try {
            return Oauth2TokenIntrospectionResponse.parse(response);
        } catch (Exception ex) {
            throw new OAuth2IntrospectionException(ex.getMessage(), ex);
        }
    }

    private Oauth2TokenIntrospectionSuccessResponse castToNimbusSuccess(Oauth2TokenIntrospectionResponse introspectionResponse) {
        if (!introspectionResponse.indicatesSuccess()) {
            throw new OAuth2IntrospectionException("Token introspection failed");
        }
        return (Oauth2TokenIntrospectionSuccessResponse) introspectionResponse;
    }

    private void validate(String token, Oauth2TokenIntrospectionSuccessResponse response) {
        // relying solely on the authorization server to validate this token (not checking 'exp', for example)
        if (!response.isAuthenticated()) {
            throw new OAuth2IntrospectionException("Provided token isn't authenticated");
        }
    }

    private OAuth2AuthenticatedPrincipal convertClaimsSet(Oauth2TokenIntrospectionSuccessResponse response) {
        Map<String, Object> claims = response.toJSONObject();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (response.getAudience() != null) {
            List<String> audiences = new ArrayList<>();
            for (Audience audience : response.getAudience()) {
                audiences.add(audience.getValue());
            }
            claims.put(AUDIENCE, Collections.unmodifiableList(audiences));
        }
        if (response.getClientID() != null) {
            claims.put(CLIENT_ID, response.getClientID().getValue());
        }
        if (response.getExpirationTime() != null) {
            Instant exp = response.getExpirationTime().toInstant();
            claims.put(EXPIRES_AT, exp);
        }
        if (response.getIssueTime() != null) {
            Instant iat = response.getIssueTime().toInstant();
            claims.put(ISSUED_AT, iat);
        }
        if (response.getIssuer() != null) {
            claims.put(ISSUER, issuer(response.getIssuer().getValue()));
        }
        if (response.getNotBeforeTime() != null) {
            claims.put(NOT_BEFORE, response.getNotBeforeTime().toInstant());
        }
        if (response.getScope() != null) {
            List<String> scopes = Collections.unmodifiableList(response.getScope().toStringList());
            claims.put(SCOPE, scopes);
        }

        List<String> authorities2 = response.getAuthorities();
        if (authorities2 != null && authorities2.size() > 0) {
            authorities
                .addAll(authorities2.stream().map(t -> new SimpleGrantedAuthority(t)).collect(Collectors.toList()));
        }
        return new DefaultOAuth2AuthenticatedPrincipal(claims, authorities);
    }

    private URL issuer(String uri) {
        try {
            return new URL(uri);
        } catch (Exception ex) {
            throw new OAuth2IntrospectionException("Invalid " + ISSUER + " value: " + uri);
        }
    }

    private OAuth2IntrospectionException onError(Throwable e) {
        return new OAuth2IntrospectionException(e.getMessage(), e);
    }
}
