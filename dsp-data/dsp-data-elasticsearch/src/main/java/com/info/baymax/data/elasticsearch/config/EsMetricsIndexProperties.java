package com.info.baymax.data.elasticsearch.config;

import io.searchbox.client.JestClient;
import io.searchbox.indices.IndicesExists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
@Configuration
@ConditionalOnExpression("!'${spring.elasticsearch.metrics.index}'.isEmpty()")
@ConfigurationProperties(prefix = EsMetricsIndexProperties.PREFIX)
public class EsMetricsIndexProperties {
    public static final String PREFIX = "spring.elasticsearch.metrics.index";

    /**
     * 索引前缀
     */
    private String prefix;

    /**
     * 索引后缀
     */
    private String dateFormat;

    public List<String> getIndies(JestClient client) {
        List<String> indies = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String index = prefix + "-" + now.format(formatter);
        try {
            do {
                indies.add(index);
                if ("yyyy-MM".equals(dateFormat) || "yyyyMM".equals(dateFormat)) {
                    now = now.minusMonths(1);
                } else if ("yyyy-ww".equals(dateFormat.toLowerCase()) || "yyyyww".equals(dateFormat.toLowerCase())) {
                    now = now.minusWeeks(1);
                }
                index = prefix + "-" + now.format(formatter);
            } while (client.execute(new IndicesExists.Builder(index).build()).isSucceeded());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug("find metrics indies:" + indies);
        return indies;
    }

}
