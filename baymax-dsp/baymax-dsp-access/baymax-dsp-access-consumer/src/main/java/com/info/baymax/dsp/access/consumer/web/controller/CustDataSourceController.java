package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.config.base.BaseEntityController;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.entity.base.BaseEntityService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.swagger.annotation.ApiModelFields;
import com.info.baymax.dsp.data.consumer.beans.source.CheckEntity;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.service.CustDataSourceService;
import com.info.baymax.dsp.data.dataset.entity.core.ProcessConfig;
import com.info.baymax.dsp.data.dataset.service.core.ProcessConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Override
    public Response<?> save(@ApiModelFields(requiredFields = {"name", "type", "attributes"}, hiddenFields = {"id",
        "enabled", "tenantId", "owner", "createTime", "creator", "lastModifiedTime",
        "lastModifier"}) CustDataSource t) {
        Response<?> response = CheckEntity.checkDataSource(t);
        if (!response.isOk()) {
            return response;
        }
        return BaseEntityController.super.save(t);
    }

    @Override
    public Response<?> update(@ApiModelFields(requiredFields = {"id", "name", "type", "attributes"}, hiddenFields = {
        "tenantId", "owner", "createTime", "creator", "lastModifiedTime", "lastModifier"}) CustDataSource t) {
        Response<?> response = CheckEntity.checkDataSource(t);
        if (!response.isOk()) {
            return response;
        }
        return BaseEntityController.super.update(t);
    }

    @Override
    public Response<IPage<CustDataSource>> page(ExampleQuery query) {
        query = ExampleQuery.builder(query);
        query.fieldGroup().andEqualTo("owner", SaasContext.getCurrentUserId());
        return BaseEntityController.super.page(query);
    }

    @ApiOperation(value = "查询数据库驱动信息", notes = "如果是JDBC类型的数据源，查询系统内置的数据库驱动信息用于用户创建数据源")
    @PostMapping("/dbDrivers")
    public Response<IPage<ProcessConfig>> dbDrivers(
        @ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        query.fieldGroup().andEqualTo("tenantId", SaasContext.getCurrentTenantId())//
            .andEqualTo("processConfigType", "jdbc driver");
        return Response.ok(processConfigService.selectPage(query));
    }

    @PostMapping("jdbc/try")
    @ApiOperation(value = "数据源链接测试")
    public Response<?> jdbcConnectionTry(@ApiParam("数据源配置信息") @RequestBody CustDataSource dataSource) {
        return Response.ok(custDataSourceService.jdbcConnect(dataSource));
    }

    @ApiOperation(value = "查询数据源所有的表名", notes = "根据ID查询数据源的数据表名称")
    @GetMapping("table/list")
    public Response<List<String>> getTableList(@ApiParam(required = true) @RequestParam String id) {
        return Response.ok(custDataSourceService.selectTableList(id));
    }
}
