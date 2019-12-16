package com.info.baymax.dsp.access.consumer.web.controller;

import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.common.jpa.page.Page;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/16 11:25
 * http://xxx.yyy/api/dsp/consumer/
 */
@Api(tags = "消费者针对数据资源的操作接口", description = "消费者数据资源操作接口")
@RestController
@RequestMapping("/dataapply")
public class DataApplicationController {
    @Autowired
    DataApplicationService dataApplicationService;

    @ApiOperation(value = "分页查询已经发布的数据资源")
    @PostMapping("/query/datares")
    public Page<DataResource> queryDataResource(QueryObject queryObject) throws Exception {
        //--TODO-- 支持按照名称,创建时间等过滤
//        Page<Dataset> result = datasetServerService
//                .findPageResult(QueryObject.builder(queryObject).setCurrentTenantCondition(SaasContext.current()));
//        return result;
        return null;
    }

    @ApiOperation(value = "分页查询自己的申请记录")
    @PostMapping("/query")
    public Page<DataApplication> queryDataApplication(QueryObject queryObject) throws Exception {
        //--TODO-- 支持按照名称，Engine,创建时间等过滤
//        Page<Dataset> result = datasetServerService
//                .findPageResult(QueryObject.builder(queryObject).setCurrentTenantCondition(SaasContext.current()));
//        return result;
        return null;
    }

    @ApiOperation(value = "消费者申请数据资源")
    @PostMapping("/create")
    public Response createDataApplicaiton(DataApplication dataApplication) throws Exception {
        //--TODO-- checkEntity, saveObj ,return id;
        //checkEntity
        int id = dataApplicationService.createDataApplication(dataApplication);
        return Response.ok(id);
    }


    @ApiOperation(value = "修改DataApplication记录")
    @PutMapping("/update")
    public String updateDataApplication(DataApplication dataApplication) throws Exception {
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        dataApplicationService.updateDataApplication(dataApplication);
        return "";
    }

    @ApiOperation(value = "删除DataApplication记录")
    @DeleteMapping("/delete")
    public String deleteDataApplication(List<Integer> ids) throws Exception {
        //request boy :string[] ids
        dataApplicationService.deleteDataApplication(ids);
        return null;
    }

}
