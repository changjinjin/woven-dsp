package com.info.baymax.common.webflux.config;

import com.info.baymax.common.webflux.server.error.DefaultErrorResponseDeterminer;
import com.info.baymax.common.webflux.server.error.ErrorResponseDeterminer;
import com.info.baymax.common.webflux.server.error.GlobalErrorAttributes;
import com.info.baymax.common.webflux.server.result.ServerFilterFieldsHandlerResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@Configuration
public class WebFluxServerConfiguration {
	@Autowired
	private ServerProperties serverProperties;
	@Autowired
	private RequestedContentTypeResolver webFluxContentTypeResolver;
	@Autowired
	private ReactiveAdapterRegistry webFluxAdapterRegistry;
	@Autowired
	private ServerCodecConfigurer serverCodecConfigurer;

	@Bean
	public ServerFilterFieldsHandlerResultHandler serverFilterFieldsHandlerResultHandler() {
		return new ServerFilterFieldsHandlerResultHandler(serverCodecConfigurer.getWriters(),
			webFluxContentTypeResolver, webFluxAdapterRegistry);
	}

	@Bean
	@ConditionalOnMissingBean(value = ErrorResponseDeterminer.class)
	public ErrorResponseDeterminer errorResponseDeterminer() {
		return new DefaultErrorResponseDeterminer();
	}

	@Bean
	@Primary
	public GlobalErrorAttributes errorAttributes(@Autowired final ErrorResponseDeterminer errorResponseDeterminer) {
		return new GlobalErrorAttributes(this.serverProperties.getError().isIncludeException(),
			errorResponseDeterminer);
	}

}
