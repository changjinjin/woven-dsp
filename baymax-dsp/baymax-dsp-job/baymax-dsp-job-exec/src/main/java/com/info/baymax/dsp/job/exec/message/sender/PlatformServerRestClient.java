package com.info.baymax.dsp.job.exec.message.sender;

import com.info.baymax.common.feign.FeignClientConfiguration;
import com.info.baymax.dsp.data.dataset.entity.ConfigItem;
import com.info.baymax.dsp.data.dataset.entity.core.FlowSchedulerDesc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 1. 任务提交,生成并执行FlowSchedulerDes
 * 2. 查询任务当前状态,及如何更新增量值
 */
@FeignClient(qualifier = "platformServerRestClient",  name = "platform", configuration = FeignClientConfiguration.class)
public interface PlatformServerRestClient {
    @PostMapping(value = "/api/schedulers")
    Object runScheduler(@RequestBody FlowSchedulerDesc scheduler) throws Exception;

    @GetMapping(value = "/api/flows/{id}/runtime-properties")
    List<ConfigItem> getRuntimeProperties(@PathVariable("id") String id) throws Exception;

}
