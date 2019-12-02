//package com.jusfoun.services.auth.server.web.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jusfoun.common.base.controller.BaseIdableController;
//import com.jusfoun.common.base.service.BaseIdableService;
//import com.jusfoun.common.base.service.BaseService;
//import com.jusfoun.common.base.tree.BaseTreeableAndIdableController;
//import com.jusfoun.common.base.tree.TreeableAndIdableService;
//import com.jusfoun.services.auth.api.entity.SysPrivs;
//import com.jusfoun.services.auth.server.service.SysPrivsService;
//
//import io.swagger.annotations.Api;
//
///**
// * 说明：权限管理接口. <br>
// *
// * @author yjw@jusfoun.com
// * @date 2017年9月23日 下午5:37:13
// */
//@Api(tags = "权限管理", description = "系统权限维护管理", value = "权限维护管理接口类")
//@RestController
//@RequestMapping(value = { "/v1/sysprivs", "/app/sysprivs" })
//public class SysPrivsController
//		implements BaseIdableController<SysPrivs, Long>, BaseTreeableAndIdableController<SysPrivs, Long> {
//
//	@Autowired
//	private SysPrivsService sysPrivsService;
//
//	@Override
//	public BaseService<SysPrivs> getBaseService() {
//		return sysPrivsService;
//	}
//
//	@Override
//	public BaseIdableService<SysPrivs> getBaseIdableService() {
//		return sysPrivsService;
//	}
//
//	@Override
//	public TreeableAndIdableService<SysPrivs, Long> getTreeableIdableService() {
//		return sysPrivsService;
//	}
//
//}
