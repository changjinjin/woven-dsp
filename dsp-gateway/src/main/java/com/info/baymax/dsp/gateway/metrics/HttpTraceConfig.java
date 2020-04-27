package com.info.baymax.dsp.gateway.metrics;

import com.info.baymax.dsp.gateway.web.mothed.RequestUriMappingsHolder;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "management.trace.http", name = {"enabled"}, matchIfMissing = true)
@EnableConfigurationProperties(HttpTraceProperties.class)
@AutoConfigureBefore(HttpTraceAutoConfiguration.class)
public class HttpTraceConfig {

    @Autowired
    private RequestUriMappingsHolder holder;

    @Bean
    // @ConditionalOnMissingBean(HttpTraceRepository.class)
    public RequestCountTraceRepository traceRepository() {
        return new RequestCountTraceRepository();
    }

    @Bean
    // @ConditionalOnMissingBean({ WebFluxTagsProvider.class })
    public DefaultWebFluxTagsProvider webfluxTagConfigurer() {
        return new DefaultWebFluxTagsProvider(holder);
    }

    @Bean
    public MeterFilter meterFilter() {
        // todo filter system default metrics
        return new MeterFilter() {
            @Override
            public MeterFilterReply accept(Meter.Id id) {
                if (id.getName().contains("hikaricp.connections") || id.getName().contains("jdbc.connections")
                    || "log4j2.events".equals(id.getName())) {
                    return MeterFilterReply.DENY;
                }
                return MeterFilterReply.NEUTRAL;
            }
        };
    }
}
