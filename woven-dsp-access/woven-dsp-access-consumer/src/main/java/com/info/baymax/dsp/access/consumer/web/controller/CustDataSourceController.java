package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.dataset.entity.core.ProcessConfig;
import com.info.baymax.dsp.data.dataset.service.core.ProcessConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dss")
@Api(tags = "消费端: 数据源管理接口", value = "数据源管理接口")
public class CustDataSourceController implements BaseEntityController<CustDataSource> {
    @Autowired
    private CustDataSourceService custDataSourceService;
    @Autowired
    private ProcessConfigService processConfigService;

    @Override
    public BaseEntityService<CustDataSource> getBaseEntityService() {
        return custDataSourceService;
    }

    @ApiOperation(value = "查询数据库驱动信息", notes = "如果是JDBC类型的数据源，查询系统内置的数据库驱动信息用于用户创建数据源")
    @PostMapping("/dbDrivers")
    @ResponseBody
    public Response<IPage<ProcessConfig>> dbDrivers(
        @ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        if (query == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询条件不能为空");
        }
        query = ExampleQuery.builder(query)//
            .fieldGroup()//
            .andEqualTo("tenantId", SaasContext.getCurrentTenantId())//
            .andEqualTo("processConfigType", "jdbc driver")//
            .end();
        return Response.ok(processConfigService.selectPage(query));
    }

}
