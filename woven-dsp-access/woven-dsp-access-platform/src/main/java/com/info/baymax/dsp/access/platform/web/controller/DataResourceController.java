package com.info.baymax.dsp.access.platform.web.controller;

import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.jpa.page.Page;
import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: haijun
 * @Date: 2019/12/13 16:41
 */
@Api(tags = "数据资源相关接口", description = "数据资源相关接口")
@RestController
@RequestMapping("/datares")
public class DataResourceController {
    @ApiOperation(value = "分页查询数据集")
    @PostMapping("/query/datasets")
    public Page<Object> queryDataset(QueryObject queryObject) throws Exception {
//        Page<Dataset> result = datasetServerService
//                .findPageResult(QueryObject.builder(queryObject).setCurrentTenantCondition(SaasContext.current()));
//        return result;
        return null;
    }


    //新建dataResource记录
    @PostMapping
    public Response createDataResource(DataResource drs) throws Exception {
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        Response res = new Response(200);
        return res.message("").content(null);
    }


    //更新dataResource记录
    @PutMapping
    public String updateDataResource(DataResource drs) throws Exception {
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        return "id";
    }

    //删除dataResource记录
    @DeleteMapping
    public String deleteDataResource(String[] ids) throws Exception {
        //checkEntity
        //dataSourceService.saveOrUpdate(drs);
        return null;
    }

    //开放数据资源给消费者
    @PostMapping
    public String openDataResource(String[] ids) throws Exception {

        return null;
    }


}
