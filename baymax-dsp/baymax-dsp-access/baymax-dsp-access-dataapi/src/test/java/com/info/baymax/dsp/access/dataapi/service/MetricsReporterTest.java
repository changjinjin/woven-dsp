package com.info.baymax.dsp.access.dataapi.service;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.merce.woven.metrics.report.MetricsReporter;

public class MetricsReporterTest extends AbstractBootTest {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private MetricsReporter reporter;

    @Test
    public void report() {
        for (int i = 0; i < 10000; i++) {
            reporter.report(MetricsEntity.TYPE_NAME, appName, System.nanoTime(),
                MetricsEntity.builder().title("title_" + i).weight(i).birth(new Date()).build());
        }
    }
}