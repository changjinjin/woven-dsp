package com.info.baymax.common.webflux.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.webflux.server.error.DefaultErrorResponseDeterminer;
import com.info.baymax.common.webflux.server.error.ErrorResponseDeterminer;
import com.info.baymax.common.webflux.server.error.GlobalErrorAttributes;
import com.info.baymax.common.webflux.server.result.ServerFilterFieldsHandlerResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.*;
import reactor.core.scheduler.Schedulers;

@Slf4j
@EnableWebFlux
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomWebFluxConfig implements WebFluxConfigurer {
	@Autowired
	@Nullable
	private WebProperties webProperties;
	@Autowired
	private ObjectMapper objectMapper;

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
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (webProperties != null) {
			Resources resources = webProperties.getResources();
			String[] staticLocations = resources.getStaticLocations();
			if (resources.isAddMappings() && staticLocations != null && staticLocations.length > 0) {
				ResourceHandlerRegistration resourceHandler = registry.addResourceHandler("/**");
				for (String staticLocation : staticLocations) {
					resourceHandler.addResourceLocations(staticLocation);
				}
			}
		}
	}

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();
		partReader.setBlockingOperationScheduler(Schedulers.immediate());
		configurer.defaultCodecs().multipartReader(new MultipartHttpMessageReader(partReader));

		// SynchronossPartHttpMessageReader syncPartReader = new SynchronossPartHttpMessageReader();
		// partReader.setMaxParts(Integer.parseInt(commonConfig.getMaxparts()));
		// 字节bytes
		// partReader.setMaxDiskUsagePerPart(Integer.parseInt(commonConfig.getMaxFileSize()));
		// partReader.setEnableLoggingRequestDetails(true);

		// 单文件上传大小限制
		// MultipartHttpMessageReader multipartReader = new MultipartHttpMessageReader(partReader);
		// multipartReader.setEnableLoggingRequestDetails(true);
		// configurer.defaultCodecs().multipartReader(multipartReader);

		// 配置jackson
		ServerCodecConfigurer.ServerDefaultCodecs defaultCodecs = configurer.defaultCodecs();
		defaultCodecs.enableLoggingRequestDetails(true);
		defaultCodecs.jackson2JsonDecoder(
			new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.APPLICATION_NDJSON));
		defaultCodecs.jackson2JsonEncoder(
			new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.APPLICATION_NDJSON));
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
