package com.info.baymax.common.elasticsearch.multiple.service.elasticsearch.impl;

import com.info.baymax.common.elasticsearch.multiple.entity.elasticsearch.EsStudent;
import com.info.baymax.common.elasticsearch.multiple.mybatis.mapper.elasticsearch.EsStudentMapper;
import com.info.baymax.common.elasticsearch.multiple.service.elasticsearch.EsStudentService;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsStudentServiceImpl extends EntityClassServiceImpl<EsStudent> implements EsStudentService {

	@Autowired
	private EsStudentMapper esStudentMapper;

	@Override
	public MyIdableMapper<EsStudent> getMyIdableMapper() {
		return esStudentMapper;
	}

}
