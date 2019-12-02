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
//import com.jusfoun.services.auth.api.entity.SysGov;
//import com.jusfoun.services.auth.server.service.SysGovService;
//
//import io.swagger.annotations.Api;
//
///**
// * 说明： 组织机构管理. <br>
// *
// * @author yjw@jusfoun.com
// * @date 2017年9月23日 上午10:07:10
// */
//@Api(tags = "组织机构管理", description = "系统组织机构管理", value = "组织机构管理接口类")
//@RestController
//@RequestMapping(value = { "/v1/sysgov", "/app/sysgov" })
//public class SysGovController
//		implements BaseIdableController<SysGov, Long>, BaseTreeableAndIdableController<SysGov, Long> {
//
//	@Autowired
//	private SysGovService sysGovService;
//
//	@Override
//	public BaseService<SysGov> getBaseService() {
//		return sysGovService;
//	}
//
//	@Override
//	public BaseIdableService<SysGov> getBaseIdableService() {
//		return sysGovService;
//	}
//
//	@Override
//	public TreeableAndIdableService<SysGov, Long> getTreeableIdableService() {
//		return sysGovService;
//	}
//
//}
