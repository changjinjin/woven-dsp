package com.info.baymax.dsp.access.platform.web.controller;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.jpa.page.Page;
import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import com.info.baymax.dsp.data.platform.service.impl.DataResourceServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 16:41
 * http://xxx.yyy/api/dsp/platform/
 */
@Api(tags = "数据资源相关接口", description = "数据资源相关接口")
@RestController
@RequestMapping("/datares")
public class DataResourceController {
    @Autowired
    DataResourceService dataResourceService;

    @ApiOperation(value = "分页查询Baymax数据集")
    @PostMapping("/query/datasets")
    public Page<DataResource> queryDataset(QueryObject queryObject) throws Exception {
        //--TODO-- 查询merce_dataset记录,支持按照名称，Engine,创建时间等过滤
//        Page<Dataset> result = datasetServerService
//                .findPageResult(QueryObject.builder(queryObject).setCurrentTenantCondition(SaasContext.current()));
//        return result;
        return null;
    }

    @ApiOperation(value = "分页查询关联后的数据资源")
    @PostMapping("/query")
    public Page<DataResource> queryDataResource(QueryObject queryObject) throws Exception {
        //--TODO-- 支持按照名称，Engine， 发布状态，创建时间等过滤
//        Page<Dataset> result = datasetServerService
//                .findPageResult(QueryObject.builder(queryObject).setCurrentTenantCondition(SaasContext.current()));
//        return result;
        return null;
    }

    @ApiOperation(value = "新建dataResource记录")
    @PostMapping("/create")
    public Response createDataResource(DataResource drs) throws Exception {
        //--TODO-- checkEntity, saveObj ,return id;
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        int id = dataResourceService.createDataResource(drs);
        return Response.ok(id);
    }


    @ApiOperation(value = "更新dataResource记录")
    @PutMapping("/update")
    public String updateDataResource(DataResource drs) throws Exception {
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        dataResourceService.updateDataResource(drs);
        return "";
    }

    @ApiOperation(value = "删除dataResource记录")
    @DeleteMapping("/delete")
    public String deleteDataResource(List<Integer> ids) throws Exception {
        //request boy :string[] ids
        dataResourceService.deleteDataResource(ids);
        return null;
    }

    @ApiOperation(value = "开放数据资源给消费者")
    @PostMapping("/open")
    public String openDataResource(DataResource drs) throws Exception {
        //--TODO-- updateOpenStatus 1 and updateDataPolicy

        return null;
    }

    @ApiOperation(value = "关闭某数据资源的申请权限")
    @PostMapping("/close")
    public String closeDataResource(List<Integer> ids) throws Exception {
        //--TODO-- 根据dataResourceId关联更新consumer_data_application record status,禁止申请权限,记录对消费者不可见
        //--TODO-- updateOpenStatus 0

        return null;
    }
}
