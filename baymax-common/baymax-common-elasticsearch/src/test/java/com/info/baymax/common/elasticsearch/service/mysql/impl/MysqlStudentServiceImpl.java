package com.info.baymax.common.elasticsearch.service.mysql.impl;

import com.info.baymax.common.elasticsearch.entity.mysql.MysqlStudent;
import com.info.baymax.common.elasticsearch.mybatis.mapper.mysql.MysqlStudentMapper;
import com.info.baymax.common.elasticsearch.service.mysql.MysqlStudentService;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MysqlStudentServiceImpl extends EntityClassServiceImpl<MysqlStudent> implements MysqlStudentService {

	@Autowired
	private MysqlStudentMapper mysqlStudentMapper;

	@Override
	public MyIdableMapper<MysqlStudent> getMyIdableMapper() {
		return mysqlStudentMapper;
	}

}
