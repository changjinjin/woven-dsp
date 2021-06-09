package com.info.baymax.common.elasticsearch.service.impl;

import com.info.baymax.common.elasticsearch.entity.TStudent;
import com.info.baymax.common.elasticsearch.mybatis.mapper.TStudentMapper;
import com.info.baymax.common.elasticsearch.service.TStudentService;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TStudentServiceImpl extends EntityClassServiceImpl<TStudent> implements TStudentService {

	@Autowired
	private TStudentMapper tStudentMapper;

	@Override
	public MyIdableMapper<TStudent> getMyIdableMapper() {
		return tStudentMapper;
	}

}
