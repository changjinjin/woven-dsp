//package com.jusfoun.services.auth.server.config.oauth2;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.util.Assert;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//
//public class OAuth2FeignRequestInterceptor implements RequestInterceptor {
//
//	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
//
//	private static final String AUTHORIZATION_HEADER = "Authorization";
//
//	private static final String BEARER_TOKEN_TYPE = "Bearer";
//
//	private final OAuth2RestTemplate oAuth2RestTemplate;
//
//	public OAuth2FeignRequestInterceptor(OAuth2RestTemplate oAuth2RestTemplate) {
//		Assert.notNull(oAuth2RestTemplate, "Context can not be null");
//		this.oAuth2RestTemplate = oAuth2RestTemplate;
//	}
//
//	@Override
//	public void apply(RequestTemplate template) {
//		LOGGER.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE);
//		template.header(AUTHORIZATION_HEADER,
//				String.format("%s %s", BEARER_TOKEN_TYPE, oAuth2RestTemplate.getAccessToken().toString()));
//
//	}
//
//}
