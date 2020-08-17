package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.query.field.Field;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.field.SqlEnums.Operator;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.common.queryapi.result.MapEntity;
import com.info.baymax.common.queryapi.result.Response;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.dataset.entity.core.DataSource;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.service.core.DataSourceService;
import com.info.baymax.dsp.data.dataset.service.security.ResourceDescService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "数据管理：数据源查询接口", description = "Baymax服务平台中datasource数据查询相关接口，用于关联转化数据资源")
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    private ResourceDescService resourceDescService;
    @Autowired
    private DataSourceService dataSourceService;

    @ApiOperation(value = "数据源分页查询", notes = "根据条件分页查询数据，复杂的查询条件需要构建一个ExampleQuery对象")
    @PostMapping("/page")
    public Response<IPage<DataSource>> page(
        @ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        FieldGroup fieldGroup = query.fieldGroup();
        List<Field> feilds = fieldGroup.getFields();
        if (ICollections.hasElements(feilds)) {
            for (Field field : feilds) {
                String name = field.getName();
                if ("resourceId".equals(name)) {
                    field.setOper(Operator.IN);
                    field.setValue(fetchResourceIds((String) field.getValue()[0]));
                }
            }
        }
        fieldGroup.andEqualTo("type", "DB").andEqualTo("tenantId", SaasContext.getCurrentTenantId())
            .andEqualTo("isHide", 0);
        return Response.ok(dataSourceService.selectPage(query));
    }

    public String[] fetchResourceIds(String rootId) {
        ResourceDesc root = resourceDescService.selectByPrimaryKey(rootId);
        if (root != null) {
            List<ResourceDesc> list = resourceDescService.selectList(ExampleQuery.builder(ResourceDesc.class)
                .fieldGroup(FieldGroup.builder().andRightLike("path", root.getPath())));
            if (ICollections.hasElements(list)) {
                return list.stream().map(t -> t.getId()).toArray(String[]::new);
            }
        }
        return new String[]{rootId};
    }

    @ApiOperation(value = "数据源详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/infoById")
    public Response<DataSource> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam String id) {
        return Response.ok(dataSourceService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "查询所有表", notes = "根据ID查询数据源中包含的表名")
    @GetMapping("/{dataSourceId}/tables")
    public Response<List<String>> tableList(
        @ApiParam(value = "数据源ID", required = true) @PathVariable("dataSourceId") String dataSourceId) {
        return Response.ok(dataSourceService.fetchTableList(dataSourceId));
    }

    @ApiOperation(value = "表中列查询", notes = "根据数据源ID和表名查询表中的字段")
    @GetMapping("/{dataSourceId}/{table}/columns")
    public Response<List<String>> columnList(
        @ApiParam(value = "数据源ID", required = true) @PathVariable("dataSourceId") String dataSourceId,
        @ApiParam(value = "表名", required = true) @PathVariable("table") String table) {
        return Response.ok(dataSourceService.fetchTableColumns(dataSourceId, table));
    }

    @ApiOperation(value = "表数据预览", notes = "根据sql模板和参数预览数据")
    @PostMapping("/{dataSourceId}/previewBySql")
    public Response<IPage<MapEntity>> previewBySql(
        @ApiParam(value = "数据源ID", required = true) @PathVariable("dataSourceId") String dataSourceId,
        @ApiParam(value = "SQL查询参数", required = true) @RequestBody SqlQuery query) {
        return Response.ok(dataSourceService.previewBySql(dataSourceId, query));
    }

}
