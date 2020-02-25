package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.Field;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.info.baymax.common.service.criteria.example.SqlEnums.Operator;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.dataset.service.security.ResourceDescService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "数据管理：数据集查询接口", description = "Baymax服务平台中dataset数据查询相关接口，用于关联转化数据资源")
@RestController
@RequestMapping("/dataset")
public class DatasetController {

    @Autowired
    private ResourceDescService resourceDescService;
    @Autowired
    private DatasetService datasetService;

    @ApiOperation(value = "资源目录查询", notes = "根据条件分页查询数据，复杂的查询条件需要构建一个ExampleQuery对象")
    @PostMapping("/resDirTree")
    @ResponseBody
    public Response<List<ResourceDesc>> resourceDirTree() {
        ExampleQuery query = ExampleQuery.builder(ResourceDesc.class)//
                .fieldGroup()//
                .andEqualTo("tenantId", SaasContext.getCurrentTenantId())//
                .andEqualTo("resType", "dataset_dir")//
                .end();
        return Response.ok(resourceDescService.fetchTree(resourceDescService.selectList(query)));
    }

    @ApiOperation(value = "分页查询", notes = "根据条件分页查询数据，复杂的查询条件需要构建一个ExampleQuery对象")
    @PostMapping("/page")
    @ResponseBody
    public Response<IPage<Dataset>> page(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        if (query == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询条件不能为空");
        }
        FieldGroup fieldGroup = query.fieldGroup();
        List<Field> feilds = fieldGroup.getFeilds();
        if (ICollections.hasElements(feilds)) {
            for (Field field : feilds) {
                String name = field.getName();
                if ("resourceId".equals(name)) {
                    field.setOper(Operator.IN);
                    field.setValue(fetchResourceIds((String) field.getValue()[0]));
                }
            }
        }
        fieldGroup.andEqualTo("tenantId", SaasContext.getCurrentTenantId()).andEqualTo("isHide", 0);
        return Response.ok(datasetService.selectPage(query));
    }

    public String[] fetchResourceIds(String rootId) {
        ResourceDesc root = resourceDescService.selectByPrimaryKey(rootId);
        if (root != null) {
            List<ResourceDesc> list = resourceDescService.selectList(
                    ExampleQuery.builder(ResourceDesc.class).fieldGroup().andRightLike("path", root.getPath()).end());
            if (ICollections.hasElements(list)) {
                return list.stream().map(t -> t.getId()).toArray(String[]::new);
            }
        }
        return new String[]{rootId};
    }

    @ApiOperation(value = "查询详情", notes = "根据ID查询单条数据的详情，ID不能为空")
    @GetMapping("/infoById")
    @ResponseBody
    public Response<Dataset> infoById(@ApiParam(value = "记录ID", required = true) @RequestParam String id) {
        if (id == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询记录ID不能为空");
        }
        return Response.ok(datasetService.getSingleResult(QueryObject.builder().addField("id", id)));
    }
}
