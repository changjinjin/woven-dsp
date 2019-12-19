package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/16 14:44
 * 管理员针对消费者的申请记录进行操作
 */
@Slf4j
@Api(tags = "管理员审批相关接口", description = "管理员审批相关接口")
@RestController
@RequestMapping("/application")
public class PlatDataApplicationController {
    @Autowired
    DataApplicationService dataApplicationService;

    @Autowired
    DataServiceEntityService dataServiceEntityService;

    @ApiOperation(value = "分页查询所有消费者的申请记录")
    @PostMapping("/query")
    public IPage<DataApplication> queryDataApplication(ExampleQuery exampleQuery) throws Exception {
        //--TODO-- 支持按照名称，Engine,创建时间等过滤
        return dataApplicationService.selectPage(exampleQuery);
    }

    @ApiOperation(value = "根据申请记录id查询用户申请记录详情")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response getDataResource(@PathVariable("id") Long id ) throws Exception {
        log.info("get dataApplication detail ...");
        DataApplication dataApplication = dataApplicationService.findOne(SaasContext.getCurrentTenantId(), id);
        return Response.ok(dataApplication);
    }

    @ApiOperation(value = "审批消费者申请记录")
    @PostMapping("/approval")
    public Response updateDataApplication(List<String> objects) throws Exception {
        //--TODO-- checkEntity, saveObj ,return id;
        //checkEntity
        DataApplication dataApplication = JsonBuilder.getInstance().fromJson(objects.get(0), DataApplication.class);
        DataService dataServiceEntity = JsonBuilder.getInstance().fromJson(objects.get(1), DataService.class);
        dataApplicationService.updateDataApplication(dataApplication);
        if (dataApplication.getStatus() == 1) {
            dataServiceEntityService.insert(dataServiceEntity);
        }
        return new Response().status(HttpStatus.ACCEPTED.value());
    }


    @ApiOperation(value = "删除审批记录DataApplication")
    @DeleteMapping("/delete")
    public Response deleteDataApplication(List<Long> ids) throws Exception {
        //request boy :string[] ids
        dataApplicationService.deleteByIds(SaasContext.getCurrentTenantId(), ids.toArray());
        return new Response().status(HttpStatus.NO_CONTENT.value());
    }

}
