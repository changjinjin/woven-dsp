package com.info.baymax.dsp.data.consumer.service.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.consumer.entity.CustApp;
import com.info.baymax.dsp.data.consumer.mybatis.mapper.CustAppMapper;
import com.info.baymax.dsp.data.consumer.service.CustAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
		return CustAppService.super.save(t);
	}

	@Override
	public CustApp update(CustApp t) {
		return CustAppService.super.update(t);
	}

}
