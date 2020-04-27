package com.info.baymax.dsp.gateway.feign;

import com.alibaba.fastjson.JSONObject;
import com.info.baymax.dsp.gateway.web.mothed.RestOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "dsp-access-platform", configuration = FeignClientConfiguration.class)
public interface RestOperationClient {

    /**
     * 根据条件查询接口列表
     *
     * @param t 查询条件
     * @return 结果集
     */
    @GetMapping(value = "/api/dsp/platform/menu/fetchRestOperations")
    List<JSONObject> fetchRestOperations(@RequestBody RestOperation t);
}