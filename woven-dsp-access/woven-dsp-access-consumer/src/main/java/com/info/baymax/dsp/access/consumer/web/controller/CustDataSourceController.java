package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
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
    public Response<?> save(
			/*
			 * @ApiModelFields(requiredFields = {"name", "type", "attributes"}, filterFields = {"id", "enabled",
			 * "tenantId", "owner", "createTime", "creator", "lastModifiedTime", "lastModifier"}, includeMode = false)
			 */ CustDataSource t) {
        Response<?> response = CheckEntity.checkDataSource(t);
        if (!response.success()) {
            return response;
        }
        return BaseEntityController.super.save(t);
    }

    @Override
    public Response<?> update(CustDataSource t) {
        Response<?> response = CheckEntity.checkDataSource(t);
        if (!response.success()) {
            return response;
        }
        return BaseEntityController.super.update(t);
    }

    @Override
    public Response<IPage<CustDataSource>> page(ExampleQuery query) {
        return BaseEntityController.super.page(
            ExampleQuery.builder(query).fieldGroup().andEqualTo("owner", SaasContext.getCurrentUserId()).end());
    }

    @ApiOperation(value = "查询数据库驱动信息", notes = "如果是JDBC类型的数据源，查询系统内置的数据库驱动信息用于用户创建数据源")
    @PostMapping("/dbDrivers")
    @ResponseBody
    public Response<IPage<ProcessConfig>> dbDrivers(
        @ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        if (query == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询条件不能为空");
        }
        query.getFieldGroup()
            .andEqualTo("tenantId", SaasContext.getCurrentTenantId())//
            .andEqualTo("processConfigType", "jdbc driver");
        return Response.ok(processConfigService.selectPage(query));
    }

	/*
	 * @ApiOperation(value = "测试文档注解", notes = "测试文档注解")
	 * 
	 * @PostMapping("/test")
	 * 
	 * @ResponseBody public Response<?> test(@ApiModelMap(value = {
	 * 
	 * @ApiModelProperty(value = "主键", name = "id", dataType = "long", notes = "主键", allowableValues = "1,2",
	 * allowEmptyValue = false, position = 1, required = true),
	 * 
	 * @ApiModelProperty(value = "名称", name = "name", dataType = "string", notes = "名称", position = 2)}) Map<String,
	 * Object> map) { return Response.ok(); }
	 */

}
