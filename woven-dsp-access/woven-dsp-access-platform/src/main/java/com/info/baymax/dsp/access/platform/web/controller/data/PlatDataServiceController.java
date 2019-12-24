package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseEntityController;
import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.data.platform.service.DataServiceEntityService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
