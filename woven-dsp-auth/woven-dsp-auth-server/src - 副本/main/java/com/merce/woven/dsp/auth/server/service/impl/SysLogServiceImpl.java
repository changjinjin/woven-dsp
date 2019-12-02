//package com.jusfoun.services.auth.server.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.jusfoun.common.mybatis.mapper.MyBaseMapper;
//import com.jusfoun.common.mybatis.mapper.MyIdableMapper;
//import com.jusfoun.common.mybatis.mapper.extension.BaseExtensionSelectMapper;
//import com.jusfoun.services.auth.api.entity.SysLog;
//import com.jusfoun.services.auth.server.mapper.SysLogMapper;
//import com.jusfoun.services.auth.server.service.SysLogService;
//
///**
// * 说明： 系统访问日志操作. <br>
// *
// * @author yjw@jusfoun.com
// * @date 2017年10月10日 下午2:54:20
// */
//@Service
//public class SysLogServiceImpl implements SysLogService {
//
//	@Autowired
//	private SysLogMapper sysLogMapper;
//
//	@Override
//	public MyIdableMapper<SysLog> getMyIdableMapper() {
//		return sysLogMapper;
//	}
//
//	@Override
//	public MyBaseMapper<SysLog> getMyBaseMapper() {
//		return sysLogMapper;
//	}
//
//	@Override
//	public BaseExtensionSelectMapper<SysLog> getBaseExtensionSelectMapper() {
//		return sysLogMapper;
//	}
//}
