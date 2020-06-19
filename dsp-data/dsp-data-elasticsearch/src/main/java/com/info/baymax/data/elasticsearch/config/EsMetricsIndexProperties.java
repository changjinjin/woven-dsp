package com.info.baymax.data.elasticsearch.config;

import io.searchbox.client.JestClient;
import io.searchbox.indices.IndicesExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.elasticsearch.metrics.index")
public class EsMetricsIndexProperties {

    /**
     * 索引前缀
     */
    private String prefix;

    /**
     * 索引后缀
     */
    private String dateFormat;

    public List<String> getMetricsIndex(JestClient client) {
        SimpleDateFormat sfm = new SimpleDateFormat(dateFormat);
        List<String> indies = new ArrayList<>();
        Date date = new Date();
        String index = prefix + "-" + sfm.format(date);
        try {
            do {
                indies.add(index);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if ("yyyy-MM".equals(dateFormat) || "yyyyMM".equals(dateFormat)) {
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 依次取前一个月
                } else if ("yyyy-ww".equals(dateFormat.toLowerCase()) || "yyyyww".equals(dateFormat.toLowerCase())) {
                    calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - 1); // 依次取前一周
                }
                date = calendar.getTime();
                index = prefix + "-" + sfm.format(date);
            } while (client.execute(new IndicesExists.Builder(index).build()).isSucceeded());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return indies;
    }

}
