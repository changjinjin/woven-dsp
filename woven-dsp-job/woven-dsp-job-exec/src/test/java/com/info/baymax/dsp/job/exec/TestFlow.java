package com.info.baymax.dsp.job.exec;

import com.info.baymax.common.utils.JsonBuilder;
import com.info.baymax.dsp.data.consumer.entity.CustDataSource;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import com.info.baymax.dsp.data.dataset.entity.core.FlowDesc;
import com.info.baymax.dsp.data.dataset.entity.core.FlowSchedulerDesc;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import com.info.baymax.dsp.data.platform.entity.DataService;
import com.info.baymax.dsp.job.exec.util.FlowGenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/23 19:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExecutorServer.class)
@TestPropertySource(locations = { "classpath:job-exec.properties", "classpath:job-exec-dev.properties"})
public class TestFlow {
    @Autowired
    FlowGenUtil flowGenUtil;

    @Test
    public void flowDescGenerate(){
        DataResource dataResource = new DataResource();
        dataResource.setId(100L);
        dataResource.setDatasetId("94a9b820-f140-4be8-aed4-f6dd545d1797");
        dataResource.setType(0);
        dataResource.setIncrementField("AGE");
        dataResource.setEnabled(1);
        dataResource.setName("test_dataresource");
        dataResource.setOpenStatus(1);
        dataResource.setStorage("HDFS");
        dataResource.setTenantId("4566632217889");
        dataResource.setOwner("21342423545");

        DataApplication dataApplication = new DataApplication();
        dataApplication.setId(101L);
        dataApplication.setDataResId(100L);
        dataApplication.setTransferType(1);
        dataApplication.setName("test_apply");
        Map<String,String> fields = new HashMap<>();
        fields.put("ID", "id");
        fields.put("NAME", "name");
        fields.put("AGE", "age");
        fields.put("CITY", "city");
        fields.put("CREATETIME", "createtime");
        dataApplication.setFieldMappings(fields);

        CustDataSource custDataSource = new CustDataSource();
        custDataSource.setId(102L);
        custDataSource.setType("HDFS");
        Map<String,String> configs = new HashMap<>();
        configs.put("", "");

        dataApplication.setCustDataSourceId(102L);


        DataService dataService = new DataService();
        dataService.setApplicationId(101L);
        dataService.setExecutedTimes(0);
        dataService.setEnabled(1);
        dataService.setId(105L);
        dataService.setScheduleType("once");
        dataService.setStatus(1);

        try {
            FlowDesc flowDesc = flowGenUtil.generateDataServiceFlow(dataService,dataApplication, dataResource, custDataSource);
            System.out.println(JsonBuilder.getInstance().toJson(flowDesc));
            FlowSchedulerDesc scheduler = flowGenUtil.generateScheduler(dataService, flowDesc);
            System.out.println(JsonBuilder.getInstance().toJson(scheduler));
        }catch (Exception e){

        }
    }
}
