package com.info.baymax.dsp.access.consumer;

import com.info.baymax.common.utils.crypto.RSAGenerater;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {DspConsumerStarter.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataCustAppTest {

    @Autowired
    private DataCustAppService dataCustAppService;

    @Test
    public void initAppSecertkey() {
        List<DataCustApp> list = dataCustAppService.selectAll();
        RSAGenerater rsa = null;
        for (DataCustApp t : list) {
            rsa = new RSAGenerater();
            t.setPrivateKey(rsa.getPrivateKey());
            t.setPublicKey(rsa.getPublicKey());
        }
        dataCustAppService.updateListByPrimaryKey(list);
    }
}
