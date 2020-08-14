package com.info.baymax.data.elasticsearch.jest;

import com.info.baymax.common.queryapi.exception.ServiceException;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.data.elasticsearch.AbstractBootTest;
import com.info.baymax.data.elasticsearch.entity.DataTransferRecord;
import com.info.baymax.data.elasticsearch.service.DataTransferRecordService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class DataTransferRecordServiceTest extends AbstractBootTest {
    @Autowired
    private DataTransferRecordService dataTransferRecordService;

    @Test
    public void userVisitTopN() throws ServiceException {
        List<Map<String, Object>> datasetVisitTopN = dataTransferRecordService.userVisitTopN(0, 0, 10, false);
        System.out.println(datasetVisitTopN);
    }

    @Test
    public void datasetVisitTopN() throws ServiceException {
        List<Map<String, Object>> datasetVisitTopN = dataTransferRecordService.datasetVisitTopN(0, 0, 10, false);
        System.out.println(datasetVisitTopN);
    }

    @Test
    public void userVisitDatasetTopN() throws ServiceException {
        List<Map<String, Object>> userVisitDatasetTopN = dataTransferRecordService
            .datasetVisitUserTopN("4b2247e9-3d6d-4e48-a302-2a6e3e930b8f", 0, 0, 10, false);
        System.out.println(userVisitDatasetTopN);
    }

    @Test
    public void datasetVisitUserTopN() throws ServiceException {
        List<Map<String, Object>> userVisitDatasetTopN = dataTransferRecordService
            .datasetVisitUserTopN("e2a76cdc-4f56-459c-9ca9-1a0d70b668cf", 0, 0, 10, false);
        System.out.println(userVisitDatasetTopN);
    }

    @Test
    public void queryList() throws ServiceException {
        IPage<DataTransferRecord> queryList = dataTransferRecordService.queryList(null, null, null,
            "4b2247e9-3d6d-4e48-a302-2a6e3e930b8f", "e2a76cdc-4f56-459c-9ca9-1a0d70b668cf", 0, 0, 1, 100);
        System.out.println(queryList);
    }

}
