package com.info.baymax.common.webflux.config;

import com.info.baymax.common.webflux.server.error.DefaultErrorResponseDeterminer;
import com.info.baymax.common.webflux.server.error.ErrorResponseDeterminer;
import com.info.baymax.common.webflux.server.error.GlobalErrorAttributes;
import com.info.baymax.common.webflux.server.result.ServerFilterFieldsHandlerResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.scheduler.Schedulers;

@Slf4j
@EnableWebFlux
@Configuration
public class WebFluxExtConfig implements WebFluxConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 设置允许跨域的路由
		registry.addMapping("/**")
			// 是否允许证书（cookies）
			.allowCredentials(true)
			// 设置允许跨域请求的域名
			.allowedOriginPatterns("*")
			// 设置允许的方法
			.allowedMethods("*")
			// 设置header
			.allowedHeaders("*")
			// 跨域允许时间
			.maxAge(3600);
	}


	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();
		partReader.setBlockingOperationScheduler(Schedulers.immediate());
		configurer.defaultCodecs().multipartReader(new MultipartHttpMessageReader(partReader));
	}


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
