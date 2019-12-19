package com.info.baymax.dsp.data.consumer.service.impl;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.consumer.entity.CustApp;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.CustAppMapper;
import com.info.baymax.dsp.data.consumer.service.CustAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
public class CustAppServiceImpl extends EntityClassServiceImpl<CustApp> implements CustAppService {

    @Autowired
    private CustAppMapper custAppMapper;

    @Override
    public MyIdableMapper<CustApp> getMyIdableMapper() {
        return custAppMapper;
    }

    @Override
    public CustApp save(CustApp t) {
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
        return CustAppService.super.save(t);
    }

    @Override
    public CustApp update(CustApp t) {

        // key不能更新
        t.setAccessKey(null);
        return CustAppService.super.update(t);
    }

}
