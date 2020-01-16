package com.info.baymax.dsp.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReactiveClientConfig {

	@Bean
	public ReactiveClientRegistrationRepository clientRegistrationRepository() {
		Map<String, Object> provider = new HashMap<>();
		provider.put("tokenUri", "http://localhost:8004/api/auth/oauth/token");
		provider.put("authorizationUri", "http://www.server.com:9020/oauth2/authorization/reactive-client");
		provider.put("userInfoUri", "http://localhost:8004/api/auth/oauth/userinfo");
		provider.put("userNameAttribute", "username");
		provider.put("jwkSetUri", "http://localhost:8004/api/auth/oauth/.well-known/jwks.json");

		ClientRegistration customClient1 = ClientRegistration.withRegistrationId("reactive-client")
				.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)//
				.clientId("reactive_client").clientName("My Reactive Client").clientSecret("123456")
				.userNameAttributeName("username")//
				.scope("userinfo", "resource", "api", "read", "write")
				.redirectUriTemplate("http://localhost:8004/login/oauth2/code/reactive-client")
				.authorizationUri("http://localhost:8004/oauth2/authorization/reactive-client")
				.tokenUri("http://localhost:8004/api/auth/oauth/token")//
				.userInfoUri("http://localhost:8004/api/auth/oauth/userinfo")//
				.providerConfigurationMetadata(provider)//
				.build();

		return new InMemoryReactiveClientRegistrationRepository(customClient1);
	}

	@Bean
	WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepository,
			ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrationRepository, authorizedClientRepository);
		oauth.setDefaultOAuth2AuthorizedClient(true);
		return WebClient.builder().filter(oauth).build();
	}

	@Bean
	public ReactiveOAuth2AuthorizedClientService authorizedClientService(
			ReactiveClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	@Bean
	public ServerOAuth2AuthorizedClientRepository authorizedClientRepository(
			ReactiveOAuth2AuthorizedClientService authorizedClientService) {
		return new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(authorizedClientService);
	}
}
