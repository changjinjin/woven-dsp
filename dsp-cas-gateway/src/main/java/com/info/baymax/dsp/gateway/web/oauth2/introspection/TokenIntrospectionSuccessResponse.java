/*
 * oauth2-oidc-sdk
 *
 * Copyright 2012-2016, Connect2id Ltd and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.info.baymax.dsp.gateway.web.oauth2.introspection;

import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.util.DateUtils;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.SuccessResponse;
import com.nimbusds.oauth2.sdk.auth.X509CertificateConfirmation;
import com.nimbusds.oauth2.sdk.http.CommonContentTypes;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.*;
import com.nimbusds.oauth2.sdk.token.AccessTokenType;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import net.jcip.annotations.Immutable;
import net.minidev.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Token introspection success response.
 *
 * <p>
 * Related specifications:
 *
 * <ul>
 * <li>OAuth 2.0 Token Introspection (RFC 7662).
 * <li>OAuth 2.0 Mutual TLS Client Authentication and Certificate Bound Access Tokens (draft-ietf-oauth-mtls-15).
 * </ul>
 */
@Immutable
public class TokenIntrospectionSuccessResponse extends TokenIntrospectionResponse implements SuccessResponse {

	/**
	 * The parameters.
	 */
	private final JSONObject params;

	/**
	 * Creates a new token introspection success response.
	 *
	 * @param params The response parameters. Must contain at least the required {@code active} parameter and not be
	 *               {@code null}.
	 */
	public TokenIntrospectionSuccessResponse(final JSONObject params) {
		if (params.isEmpty()) {
			throw new IllegalArgumentException("Missing params parameter");
		}
		this.params = params;
	}

	public boolean isAuthenticated() {
		try {
			return JSONObjectUtils.getBoolean(params, "authenticated");
		} catch (ParseException e) {
			return false;
		}
	}

	public String getName() {
		try {
			return JSONObjectUtils.getString(params, "name");
		} catch (ParseException e) {
			return null;
		}
	}

	public String getPrincipal() {
		try {
			return JSONObjectUtils.getString(params, "principal");
		} catch (ParseException e) {
			return null;
		}
	}

	public boolean isClientOnly() {
		try {
			return JSONObjectUtils.getBoolean(params, "clientOnly");
		} catch (ParseException e) {
			return false;
		}
	}

	public JSONObject getDetails() {
		try {
			return JSONObjectUtils.getJSONObject(params, "details");
		} catch (ParseException e) {
			return null;
		}
	}

	public List<Object> getAuthoritiesList() {
		try {
			return JSONObjectUtils.getList(params, "authorities");
		} catch (ParseException e) {
			return null;
		}
	}

	public List<String> getAuthorities() {
		List<Object> list = getAuthoritiesList();
		if (list != null && list.size() > 0) {
			return list.stream().map(t -> {
				try {
					return JSONObjectUtils.getString(JSONObjectUtils.parse(t.toString()), "authority");
				} catch (ParseException e) {
					throw new RuntimeException("parse json error", e);
				}
			}).collect(Collectors.toList());
		}
		return null;
	}

	public JSONObject getOauth2Request() {
		try {
			return JSONObjectUtils.getJSONObject(params, "oauth2Request");
		} catch (ParseException e) {
			return null;
		}
	}

	public JSONObject getUserAuthentication() {
		try {
			return JSONObjectUtils.getJSONObject(params, "userAuthentication");
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the scope of the token. Corresponds to the {@code scope} claim.
	 *
	 * @return The token scope, {@code null} if not specified.
	 */
	public Scope getScope() {
		try {
			return Scope.parse(JSONObjectUtils.getString(getOauth2Request(), "scope"));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the identifier of the OAuth 2.0 client that requested the token. Corresponds to the {@code client_id}
	 * claim.
	 *
	 * @return The client identifier, {@code null} if not specified.
	 */
	public ClientID getClientID() {

		try {
			return new ClientID(JSONObjectUtils.getString(getOauth2Request(), "clientId"));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the username of the resource owner who authorised the token. Corresponds to the {@code username} claim.
	 *
	 * @return The username, {@code null} if not specified.
	 */
	public String getUsername() {
		return getName();
	}

	/**
	 * Returns the access token type. Corresponds to the {@code token_type} claim.
	 *
	 * @return The token type, {@code null} if not specified.
	 */
	public AccessTokenType getTokenType() {

		try {
			return new AccessTokenType(JSONObjectUtils.getString(getDetails(), "tokenType"));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the token expiration time. Corresponds to the {@code exp} claim.
	 *
	 * @return The token expiration time, {@code null} if not specified.
	 */
	public Date getExpirationTime() {
		try {
			return DateUtils.fromSecondsSinceEpoch(JSONObjectUtils.getLong(params, "exp"));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the token issue time. Corresponds to the {@code iat} claim.
	 *
	 * @return The token issue time, {@code null} if not specified.
	 */
	public Date getIssueTime() {
		try {
			return DateUtils.fromSecondsSinceEpoch(JSONObjectUtils.getLong(params, "iat"));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the token not-before time. Corresponds to the {@code nbf} claim.
	 *
	 * @return The token not-before time, {@code null} if not specified.
	 */
	public Date getNotBeforeTime() {
		try {
			return DateUtils.fromSecondsSinceEpoch(JSONObjectUtils.getLong(params, "nbf"));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the subject of the token, usually a machine-readable identifier of the resource owner who authorised the
	 * token. Corresponds to the {@code sub} claim.
	 *
	 * @return The token subject, {@code null} if not specified.
	 */
	public Subject getSubject() {
		try {
			return new Subject(JSONObjectUtils.getString(params, "sub"));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the intended audience for the token. Corresponds to the {@code aud} claim.
	 *
	 * @return The token audience, {@code null} if not specified.
	 */
	public List<Audience> getAudience() {
		// Try string array first, then string
		try {
			return Audience.create(JSONObjectUtils.getStringList(params, "aud"));
		} catch (ParseException e) {
			try {
				return new Audience(JSONObjectUtils.getString(params, "aud")).toSingleAudienceList();
			} catch (ParseException e2) {
				return null;
			}
		}
	}

	/**
	 * Returns the token issuer. Corresponds to the {@code iss} claim.
	 *
	 * @return The token issuer, {@code null} if not specified.
	 */
	public Issuer getIssuer() {
		try {
			return new Issuer(JSONObjectUtils.getString(params, "iss"));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the token identifier. Corresponds to the {@code jti} claim.
	 *
	 * @return The token identifier, {@code null} if not specified.
	 */
	public JWTID getJWTID() {
		try {
			return new JWTID(JSONObjectUtils.getString(params, "jti"));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the client X.509 certificate SHA-256 thumbprint, for a mutual TLS client certificate bound access token.
	 * Corresponds to the {@code cnf.x5t#S256} claim.
	 *
	 *
	 * @return The client X.509 certificate SHA-256 thumbprint, {@code null} if not specified.
	 */
	@Deprecated
	public Base64URL getX509CertificateSHA256Thumbprint() {
		try {
			JSONObject cnf = JSONObjectUtils.getJSONObject(params, "cnf", null);

			if (cnf == null)
				return null;

			String x5t = JSONObjectUtils.getString(cnf, "x5t#S256", null);

			if (x5t == null)
				return null;

			return new Base64URL(x5t);

		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the client X.509 certificate confirmation, for a mutual TLS client certificate bound access token.
	 * Corresponds to the {@code cnf.x5t#S256} claim.
	 *
	 * @return The client X.509 certificate confirmation, {@code null} if not specified.
	 */
	public X509CertificateConfirmation getX509CertificateConfirmation() {

		return X509CertificateConfirmation.parse(params);
	}

	/**
	 * Returns the string parameter with the specified name.
	 *
	 * @param name The parameter name. Must not be {@code null}.
	 *
	 * @return The parameter value, {@code null} if not specified or if parsing failed.
	 */
	public String getStringParameter(final String name) {

		try {
			return JSONObjectUtils.getString(params, name, null);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the boolean parameter with the specified name.
	 *
	 * @param name The parameter name. Must not be {@code null}.
	 *
	 * @return The parameter value.
	 *
	 * @throws ParseException If the parameter isn't specified or parsing failed.
	 */
	public boolean getBooleanParameter(final String name) throws ParseException {

		return JSONObjectUtils.getBoolean(params, name);
	}

	/**
	 * Returns the number parameter with the specified name.
	 *
	 * @param name The parameter name. Must not be {@code null}.
	 *
	 * @return The parameter value, {@code null} if not specified or parsing failed.
	 */
	public Number getNumberParameter(final String name) {

		try {
			return JSONObjectUtils.getNumber(params, name, null);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the string list parameter with the specified name.
	 *
	 * @param name The parameter name. Must not be {@code null}.
	 *
	 * @return The parameter value, {@code null} if not specified or if parsing failed.
	 */
	public List<String> getStringListParameter(final String name) {

		try {
			return JSONObjectUtils.getStringList(params, name, null);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the JSON object parameter with the specified name.
	 *
	 * @param name The parameter name. Must not be {@code null}.
	 *
	 * @return The parameter value, {@code null} if not specified or if parsing failed.
	 */
	public JSONObject getJSONObjectParameter(final String name) {

		try {
			return JSONObjectUtils.getJSONObject(params, name, null);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Returns the underlying parameters.
	 *
	 * @return The parameters, as JSON object.
	 */
	public JSONObject getParameters() {

		return params;
	}

	/**
	 * Returns a JSON object representation of this token introspection success response.
	 *
	 * <p>
	 * Example JSON object:
	 *
	 * <pre>
	 * {
	 *  "active"          : true,
	 *  "client_id"       : "l238j323ds-23ij4",
	 *  "username"        : "jdoe",
	 *  "scope"           : "read write dolphin",
	 *  "sub"             : "Z5O3upPC88QrAjx00dis",
	 *  "aud"             : "https://protected.example.net/resource",
	 *  "iss"             : "https://server.example.com/",
	 *  "exp"             : 1419356238,
	 *  "iat"             : 1419350238,
	 *  "extension_field" : "twenty-seven"
	 * }
	 * </pre>
	 *
	 * @return The JSON object.
	 */
	public JSONObject toJSONObject() {

		return new JSONObject(params);
	}

	@Override
	public boolean indicatesSuccess() {

		return true;
	}

	@Override
	public HTTPResponse toHTTPResponse() {

		HTTPResponse httpResponse = new HTTPResponse(HTTPResponse.SC_OK);
		httpResponse.setContentType(CommonContentTypes.APPLICATION_JSON);
		httpResponse.setContent(params.toJSONString());
		return httpResponse;
	}

	/**
	 * Parses a token introspection success response from the specified JSON object.
	 *
	 * @param jsonObject The JSON object to parse. Must not be {@code null}.
	 *
	 * @return The token introspection success response.
	 *
	 * @throws ParseException If the JSON object couldn't be parsed to a token introspection success response.
	 */
	public static TokenIntrospectionSuccessResponse parse(final JSONObject jsonObject) throws ParseException {

		try {
			return new TokenIntrospectionSuccessResponse(jsonObject);
		} catch (IllegalArgumentException e) {
			throw new ParseException(e.getMessage(), e);
		}
	}

	/**
	 * Parses an token introspection success response from the specified HTTP response.
	 *
	 * @param httpResponse The HTTP response. Must not be {@code null}.
	 *
	 * @return The token introspection success response.
	 *
	 * @throws ParseException If the HTTP response couldn't be parsed to a token introspection success response.
	 */
	public static TokenIntrospectionSuccessResponse parse(final HTTPResponse httpResponse) throws ParseException {
		httpResponse.ensureStatusCode(HTTPResponse.SC_OK);
		JSONObject jsonObject = httpResponse.getContentAsJSONObject();
		return parse(jsonObject);
	}
}
