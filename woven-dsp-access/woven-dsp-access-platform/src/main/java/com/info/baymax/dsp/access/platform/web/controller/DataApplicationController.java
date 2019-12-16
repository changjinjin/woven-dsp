package com.info.baymax.dsp.access.platform.web.controller;

import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.jpa.page.Page;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/16 14:44
 * 管理员针对消费者的申请记录进行操作
 */
@Api(tags = "数据资源相关接口", description = "数据资源相关接口")
@RestController
@RequestMapping("/dataapply")
public class DataApplicationController {
    @Autowired
    DataApplicationService dataApplicationService;

    @ApiOperation(value = "分页查询所有消费者的申请记录")
    @PostMapping("/query")
    public Page<DataApplication> queryDataApplication(QueryObject queryObject) throws Exception {
        //--TODO-- 支持按照名称，Engine,创建时间等过滤
//        Page<Dataset> result = datasetServerService
//                .findPageResult(QueryObject.builder(queryObject).setCurrentTenantCondition(SaasContext.current()));
//        return result;
        return null;
    }

    @ApiOperation(value = "审批消费者申请记录")
    @PostMapping("/approval")
    public Response updateDataApplication(DataApplication dataApplication) throws Exception {
        //--TODO-- checkEntity, saveObj ,return id;
        //checkEntity
        dataApplicationService.updateDataApplication(dataApplication);
        return Response.ok("");
    }


    @ApiOperation(value = "删除审批记录DataApplication")
    @DeleteMapping("/delete")
    public String deleteDataApplication(List<Integer> ids) throws Exception {
        //request boy :string[] ids
        dataApplicationService.deleteDataApplication(ids);
        return null;
    }

}
