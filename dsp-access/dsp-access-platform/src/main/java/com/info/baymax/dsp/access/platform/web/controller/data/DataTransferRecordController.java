package com.info.baymax.dsp.access.platform.web.controller.data;

import com.info.baymax.common.comp.base.BaseIdableAndExampleQueryController;
import com.info.baymax.common.service.BaseIdableAndExampleQueryService;
import com.info.baymax.dsp.data.platform.entity.DataTransferRecord;
import com.info.baymax.dsp.data.platform.service.DataTransferRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "数据管理：数据传输记录管理", description = "数据传输记录管理")
@RestController
@RequestMapping("/record")
public class DataTransferRecordController implements BaseIdableAndExampleQueryController<Long, DataTransferRecord> {

    @Autowired
    private DataTransferRecordService dataTransferRecordService;

    @Override
    public BaseIdableAndExampleQueryService<Long, DataTransferRecord> getBaseIdableAndExampleQueryService() {
        return dataTransferRecordService;
    }

}