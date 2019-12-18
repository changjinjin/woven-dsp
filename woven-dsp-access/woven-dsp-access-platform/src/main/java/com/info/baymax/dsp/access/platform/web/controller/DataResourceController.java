package com.info.baymax.dsp.access.platform.web.controller;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.dsp.data.consumer.service.DataApplicationService;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.service.DataResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/13 16:41
 * http://xxx.yyy/api/dsp/platform/
 */
@Slf4j
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
    public IPage<Dataset> queryDataset(ExampleQuery exampleQuery) throws Exception {
        //--TODO-- 查询merce_dataset记录,支持按照名称，Engine,创建时间等过滤
        log.info("query dataset list ...");
        return dataResourceService.queryDatasets(exampleQuery);
    }

    @ApiOperation(value = "分页查询关联后的数据资源")
    @PostMapping("/query")
    @ResponseStatus(HttpStatus.OK)
    public IPage<DataResource> queryDataResource(ExampleQuery exampleQuery) throws Exception {
        //--TODO-- 支持按照名称，Engine， 发布状态，创建时间等过滤
		log.info("query dataResource list ...");
        return dataResourceService.selectPage(exampleQuery);
    }

    @ApiOperation(value = "新建dataResource记录")
    @PostMapping("/create")
    public Response createDataResource(DataResource drs) throws Exception {
        //--TODO-- checkEntity, saveObj ,return id;
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        log.info("create dataResource ...");
        int id = dataResourceService.createDataResource(drs);
        Response res = new Response();
        return res.status(HttpStatus.CREATED.value()).content(id);
    }

    @ApiOperation(value = "查询数据资源详情")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response getDataResource(@PathVariable("id") Long id ) throws Exception {
        log.info("get dataResource detail ...");
        DataResource dres = dataResourceService.findOne(SaasContext.getCurrentTenantId(), id +"");
        Response res = new Response();
        return res.status(HttpStatus.CREATED.value()).content(dres);
    }

    @ApiOperation(value = "更新dataResource记录")
    @PutMapping("/update")
    public Response updateDataResource(DataResource drs) throws Exception {
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        log.info("update dataResource, id={} ...", drs.getId());
        dataResourceService.updateByPrimaryKey(drs);
        Response res = new Response();
        return res.status(HttpStatus.ACCEPTED.value());
    }

    @ApiOperation(value = "删除dataResource记录")
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Response deleteDataResource(List<Long> ids) throws Exception {
        //request boy :string[] ids
        log.info("delete dataResource , ids.size={} ...", ids.size());
        dataResourceService.deleteByIds(SaasContext.getCurrentTenantId(),ids.toArray());
        Response res = new Response();
        res.status(HttpStatus.NO_CONTENT.value());
        return res;
    }

    @ApiOperation(value = "开放数据资源给消费者")
    @PostMapping("/open")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Response openDataResource(DataResource drs) throws Exception {
        //--TODO-- updateOpenStatus 1 and updateDataPolicy
        log.info("publish dataResource, id={} ...", drs.getId());
        if(drs.getOpenStatus() == 1){
            dataResourceService.updateByPrimaryKey(drs);
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
        log.info("close dataResource and delete dataApplication, ids.size={}...", ids.size());
        dataApplicationService.deleteByDataResIds(SaasContext.getCurrentTenantId(),ids);
        dataResourceService.closeDataResource(ids);
        Response res = new Response();
        return res.status(HttpStatus.ACCEPTED.value());
    }

}
