package com.info.baymax.dsp.data.consumer.service.impl;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.DataCustAppMapper;
import com.info.baymax.dsp.data.consumer.service.DataCustAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
public class DataCustAppServiceImpl extends EntityClassServiceImpl<DataCustApp> implements DataCustAppService {

    @Autowired
    private DataCustAppMapper dataCustAppMapper;

    @Override
    public MyIdableMapper<DataCustApp> getMyIdableMapper() {
        return dataCustAppMapper;
    }

    @Override
    public DataCustApp save(DataCustApp t) {
        if (StringUtils.isEmpty(t.getAppName())) {
            throw new ServiceException(ErrType.BAD_REQUEST, "appName不能为空。");
        }
        if (StringUtils.isEmpty(t.getAccessIp())) {
            throw new ServiceException(ErrType.BAD_REQUEST, "accessIp不能为空。");
        }
        if (StringUtils.isEmpty(t.getAccessKey())) {
            t.setAccessKey(UUID.randomUUID().toString());
        }

        // 绑定消费者
        t.setCustId(SaasContext.getCurrentUserId());
        t.setCustName(SaasContext.getCurrentUsername());
        return DataCustAppService.super.save(t);
    }

    @Override
    public DataCustApp update(DataCustApp t) {

        // key不能更新
        t.setAccessKey(null);
        return DataCustAppService.super.update(t);
    }

}
