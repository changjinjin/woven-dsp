//package com.jusfoun.services.auth.server.config.oauth2;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//
//import feign.RequestInterceptor;
//
//@Configuration
//public class OAuth2FeignAutoConfiguration {
//
//	@Bean
//	public RequestInterceptor oauth2FeignRequestInterceptor(@Qualifier("practiceOAuth2RestTemplate") OAuth2RestTemplate oAuth2RestTemplate) {
//		return new OAuth2FeignRequestInterceptor(oAuth2RestTemplate);
//	}
//
//}
