package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.result.Response;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceType;
import com.info.baymax.dsp.data.dataset.service.security.ResourceDescService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "数据管理：资源目录查询接口", description = "Baymax服务平台中resourceDir数据查询相关接口")
@RestController
@RequestMapping("/resourcedir")
public class ResourceDirController {

    @Autowired
    private ResourceDescService resourceDescService;

    @ApiOperation(value = "资源目录查询", notes = "根据条件查询资源目录树")
    @PostMapping("/resDirTree/{resType}")
    public Response<List<ResourceDesc>> resourceDirTree(
        @ApiParam(value = "资源类型", required = true) @PathVariable("resType") ResourceType resType) {
        ExampleQuery query = ExampleQuery.builder(ResourceDesc.class)//
            .unpaged()// 不分页，查所有
            .fieldGroup(FieldGroup.builder().andEqualTo("tenantId", SaasContext.getCurrentTenantId())
                .andEqualTo("resType", resType.name()));
        return Response.ok(resourceDescService.fetchTree(resourceDescService.selectList(query)));
    }
}
