package com.info.baymax.dsp.gateway.feign;

import com.alibaba.fastjson.JSONObject;
import com.info.baymax.common.feign.FeignClientConfiguration;
import com.info.baymax.dsp.gateway.web.method.RestOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping(value = "/api/dsp/platform/menu/fetchRestOperations")
    List<JSONObject> fetchRestOperations(@RequestBody RestOperation t);
}