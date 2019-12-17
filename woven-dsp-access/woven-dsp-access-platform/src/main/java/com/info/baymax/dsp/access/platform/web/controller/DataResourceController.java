package com.info.baymax.dsp.access.platform.web.controller;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.merce.woven.data.entity.core.Dataset;
import com.merce.woven.common.jpa.criteria.query.QueryObject;
import com.merce.woven.common.jpa.page.Page;

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

    @Autowired
    DataApplicationService dataApplicationService;

    @ApiOperation(value = "分页查询Baymax数据集")
    @PostMapping("/query/datasets")
    @ResponseStatus(HttpStatus.OK)
    public Page<Dataset> queryDataset(QueryObject queryObject) throws Exception {
        //--TODO-- 查询merce_dataset记录,支持按照名称，Engine,创建时间等过滤
        return dataResourceService.queryDatasets(queryObject);
    }

    @ApiOperation(value = "分页查询关联后的数据资源")
    @PostMapping("/query")
    @ResponseStatus(HttpStatus.OK)
    public IPage<DataResource> queryDataResource(ExampleQuery exampleQuery) throws Exception {
        //--TODO-- 支持按照名称，Engine， 发布状态，创建时间等过滤
        return dataResourceService.selectPage(exampleQuery);
    }

    @ApiOperation(value = "新建dataResource记录")
    @PostMapping("/create")
    public Response createDataResource(DataResource drs) throws Exception {
        //--TODO-- checkEntity, saveObj ,return id;
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        int id = dataResourceService.createDataResource(drs);
        Response res = new Response();
        return res.status(HttpStatus.CREATED.value()).content(id);
    }


    @ApiOperation(value = "更新dataResource记录")
    @PutMapping("/update")
    public Response updateDataResource(DataResource drs) throws Exception {
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        dataResourceService.updateDataResource(drs);
        Response res = new Response();
        return res.status(HttpStatus.ACCEPTED.value());
    }

    @ApiOperation(value = "删除dataResource记录")
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Response deleteDataResource(List<Long> ids) throws Exception {
        //request boy :string[] ids
        dataResourceService.deleteDataResource(ids);
        Response res = new Response();
        res.status(HttpStatus.NO_CONTENT.value());
        return res;
    }

    @ApiOperation(value = "开放数据资源给消费者")
    @PostMapping("/open")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Response openDataResource(DataResource drs) throws Exception {
        //--TODO-- updateOpenStatus 1 and updateDataPolicy
        if(drs.getOpenStatus() == 1){
            dataResourceService.updateDataResource(drs);
        }else{
            throw new RuntimeException("Open DataResource but openStatus is 0");
        }
        Response res = new Response();
        return res.status(HttpStatus.ACCEPTED.value());
    }

    @ApiOperation(value = "关闭某数据资源的申请权限")
    @PostMapping("/close")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Response closeDataResource(List<Long> ids) throws Exception {
        //--TODO-- 根据dataResourceId关联更新consumer_data_application record status,禁止申请权限,或者删除记录
        //--TODO-- updateOpenStatus 0
        dataApplicationService.deleteByDataResIds(ids);
        dataResourceService.closeDataResource(ids);
        Response res = new Response();
        return res.status(HttpStatus.ACCEPTED.value());
    }

}
