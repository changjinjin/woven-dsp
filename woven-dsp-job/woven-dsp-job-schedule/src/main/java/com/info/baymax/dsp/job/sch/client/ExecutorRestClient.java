package com.info.baymax.dsp.job.sch.client;

import com.info.baymax.dsp.job.sch.config.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "woven-dsp-job-exec", configuration = FeignClientConfiguration.class)
public interface ExecutorRestClient {

    @PostMapping("/dataservice/execute")
    String deployDataservice(@RequestBody Map<String, Object> body);

    @PostMapping("/dataservice/kill/{executionId}")
    String killyDataservice(@PathVariable("executionId") String executionId, @RequestBody Map<String, Object> body);
}
