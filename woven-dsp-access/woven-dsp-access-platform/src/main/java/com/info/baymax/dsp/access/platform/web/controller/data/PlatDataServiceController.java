package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.message.exception.ControllerException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/service")
@Api(tags = "数据管理： 数据服务接口", value = "数据服务接口")
public class PlatDataServiceController implements BaseEntityController<DataService> {

    @Autowired
    DataServiceEntityService dataServiceEntityService;

    @Override
    public BaseEntityService<DataService> getBaseEntityService() {
        return dataServiceEntityService;
    }

    @ApiOperation(value = "启用停用", notes = "服务启用停用接口")
    @PostMapping("/updateStatus")
    @ResponseBody
    public Response<?> updateStatus(@ApiParam(value = "启用停用对象，传ID和状态值", required = true) @RequestBody DataService t) {
        if (t == null) {
            throw new ControllerException(ErrType.BAD_REQUEST, "查询记录ID不能为空");
        }
        dataServiceEntityService.saveOrUpdate(t);
        return Response.ok();
    }

    /**
     * 数据服务一但生成就不支持修改了，如果后期允许修改涉及到很多属性的置空
     */
}
