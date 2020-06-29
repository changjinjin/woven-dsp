package com.info.baymax.dsp.access.dataapi.service;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.utils.crypto.AESUtil;
import com.info.baymax.dsp.access.dataapi.data.MapEntity;
import com.info.baymax.dsp.access.dataapi.data.Query;
import com.info.baymax.dsp.access.dataapi.web.request.DataRequest;
import com.info.baymax.dsp.access.dataapi.web.request.PullResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class SignTest extends AbstractBootTest {
    @Autowired
    private RestSignService restSignService;
    @Autowired
    private PullService pullService;

    @Test
    public void test() {
        try {
            String accessKey = "07739986-6232-4e75-9e97-fe3321919d94";
            String secretKey = restSignService.secertkey(accessKey);
            System.out.println(secretKey);
            String signKeyIfExist = restSignService.signKeyIfExist(accessKey);
            System.out.println(signKeyIfExist);
            DataRequest request = new DataRequest(accessKey, System.currentTimeMillis(), true);
            Query query = Query.builder()//
                .page(1, 3)//
                .allProperties("id", "code", "date", "project", "income", "manager")//
                .selectProperties("id", "code", "project", "income", "manager")//
                .fieldGroup()//
                .andGreaterThan("id", 2)//
                .andIn("code", new Integer[]{1, 2, 3})//
                .end()//
                .orderBy("id")//
                .orderBy("code");

            IPage<MapEntity> page = pullService.query(null, null, null, query);
            PullResponse encrypt = PullResponse.ok(page).request(request).encrypt(signKeyIfExist);
            System.out.println(JSON.toJSONString(encrypt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        try {
            String encrypt = "46fcbd7d6be5eb0c0258fb637651275f58a112f4116aac6998c19e6bfd8c7ef61af369395ac56ef162c4efec172a620fd18d5ae7e159f544ee3d3fbf8967a8d15d63b0028cf39dae481d78410ab0197b8e7c55d3a85ed798b5ab0ee0fe0df89b58a112f4116aac6998c19e6bfd8c7ef61af369395ac56ef162c4efec172a620fd18d5ae7e159f544ee3d3fbf8967a8d15d63b0028cf39dae481d78410ab0197b8e7c55d3a85ed798b5ab0ee0fe0df89b58a112f4116aac6998c19e6bfd8c7ef62a2b37cd562ee945f1fdb3abdbc6e679";
            System.out.println(AESUtil.decrypt(encrypt, "LEbpz6DTi\\VL3-2m"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
