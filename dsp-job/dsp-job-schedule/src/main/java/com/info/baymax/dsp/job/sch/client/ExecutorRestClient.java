package com.info.baymax.dsp.job.sch.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.info.baymax.common.comp.feign.FeignClientConfiguration;

@FeignClient(qualifier = "executorRestClient", name = "dsp-job-exec", configuration = FeignClientConfiguration.class)
public interface ExecutorRestClient {

    @PostMapping("/api/dsp/exec/dataservice/execute")
    String deployDataservice(@RequestBody Map<String, Object> body);

    @PostMapping("/api/dsp/exec/dataservice/kill/{executionId}")
    String killyDataservice(@PathVariable("executionId") String executionId, @RequestBody Map<String, Object> body);
}
