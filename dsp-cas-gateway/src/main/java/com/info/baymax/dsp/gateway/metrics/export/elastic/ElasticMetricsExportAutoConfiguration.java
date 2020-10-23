package com.info.baymax.dsp.gateway.metrics.export.elastic;


import io.micrometer.core.instrument.Clock;
import io.micrometer.core.ipc.http.HttpUrlConnectionSender;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.elastic.ElasticProperties;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for exporting metrics to Elastic.
 *
 * @author pengchuan.chen
 * @date 2019/6/29
 */
@Configuration
@AutoConfigureBefore({CompositeMeterRegistryAutoConfiguration.class,
        SimpleMetricsExportAutoConfiguration.class})
@AutoConfigureAfter(MetricsAutoConfiguration.class)
//@ConditionalOnBean(Clock.class)
@ConditionalOnClass(ElasticMeterRegistry.class)
@ConditionalOnProperty(prefix = "management.metrics.export.elastic", name = "enabled",
        havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ElasticProperties.class)
public class ElasticMetricsExportAutoConfiguration {

  private final ElasticProperties properties;

  public ElasticMetricsExportAutoConfiguration(ElasticProperties properties) {
    this.properties = properties;
  }

  @Bean
  @ConditionalOnMissingBean
  public ElasticConfig elasticConfig() {
    return new ElasticPropertiesConfigAdapter(this.properties);
  }

  @Bean
  public ElasticMeterRegistry elasticMeterRegistry(ElasticConfig elasticConfig, Clock clock) {
    return ElasticMeterRegistry.builder(elasticConfig)
            .clock(clock)
            .httpClient(
                    new HttpUrlConnectionSender(this.properties.getConnectTimeout(),
                            this.properties.getReadTimeout())
            )
            .build();
  }

}
