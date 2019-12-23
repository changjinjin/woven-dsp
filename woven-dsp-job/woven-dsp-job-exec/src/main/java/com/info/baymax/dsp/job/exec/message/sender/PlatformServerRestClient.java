package com.info.baymax.dsp.job.exec.message.sender;

import com.info.baymax.dsp.data.dataset.entity.core.FlowSchedulerDesc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.info.baymax.dsp.job.exec.config.FeignClientConfiguration;

/**
 * 1. 任务提交,生成并执行FlowSchedulerDes
 * 2. 查询任务当前状态,及如何更新增量值
 */
@FeignClient(qualifier = "platformServerRestClient",  name = "platform", configuration = FeignClientConfiguration.class)
public interface PlatformServerRestClient {
    @PostMapping(value = "/api/schedulers")
    Object createScheduler(@RequestBody FlowSchedulerDesc scheduler);

    @PostMapping(value = "/api/rpc/executions/{executionId}/task/{taskId}")
    void taskInfo(@PathVariable("executionId") String executionId, @PathVariable("taskId") String taskId);

}
