//package com.jusfoun.services.auth.server.web.controller;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jusfoun.common.base.controller.BaseIdableController;
//import com.jusfoun.common.base.service.BaseIdableService;
//import com.jusfoun.common.base.service.BaseService;
//import com.jusfoun.common.message.annotation.JsonBody;
//import com.jusfoun.common.message.annotation.JsonBodys;
//import com.jusfoun.common.message.exception.ControllerException;
//import com.jusfoun.common.message.exception.ServiceException;
//import com.jusfoun.common.message.result.BaseResponse;
//import com.jusfoun.common.message.result.ErrType;
//import com.jusfoun.services.auth.api.entity.SysRole;
//import com.jusfoun.services.auth.server.service.SysRoleService;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiParam;
//
///**
// * 说明：系统角色管理接口. <br>
// *
// * @author yjw@jusfoun.com
// * @date 2017年9月23日 下午4:11:34
// */
//@Api(tags = "角色管理", description = "系统角色管理", value = "系统角色管理接口类")
//@RestController
//@RequestMapping(value = { "/v1/sysrole", "/app/sysrole" })
//public class SysRoleController implements BaseIdableController<SysRole, Long> {
//
//	@Autowired
//	private SysRoleService sysRoleService;
//
//	@Override
//	public BaseService<SysRole> getBaseService() {
//		return sysRoleService;
//	}
//
//	@Override
//	public BaseIdableService<SysRole> getBaseIdableService() {
//		return sysRoleService;
//	}
//
//	// @Logable(value = "保存角色", path = "系统管理/角色管理/保存角色")
//	@Override
//	public BaseResponse<SysRole> save(//
//			@RequestBody SysRole t//
//	) {
//		if (t == null) {
//			return BaseResponse.fail(ErrType.BAD_REQUEST);
//		}
//		if (StringUtils.isEmpty(t.getName())) {
//			return BaseResponse.fail(ErrType.BAD_REQUEST, "角色名称不能为空");
//		}
//
//		// 判断角色是否已经存在
//		SysRole record = new SysRole();
//		record.setName(t.getName());
//		int count = sysRoleService.selectCount(record);
//		if (count > 0) {
//			return BaseResponse.fail(ErrType.SYSROLE_ENTITY_EXIST_ERROR);
//		}
//
//		try {
//			sysRoleService.saveWithAssociate(t);
//			return BaseResponse.success();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSROLE_SAVE_ERROR);
//		}
//	}
//
//	// @Logable(value = "修改角色", path = "系统管理/角色管理/修改角色")
//	@Override
//	public BaseResponse<SysRole> updateById(//
//			@RequestBody SysRole t//
//	) {
//		if (t == null || t.getId() == null) {
//			return BaseResponse.fail(ErrType.BAD_REQUEST);
//		}
//		try {
//			sysRoleService.updateWithAssociate(t);
//			return BaseResponse.success();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSROLE_UPDATE_ERROR);
//		}
//	}
//
//	// @Logable(value = "删除角色", path = "系统管理/角色管理/删除角色")
//	@Override
//	public BaseResponse<?> deleteById(//
//			@ApiParam(value = "角色ID", required = true) @RequestParam(required = true) Long id//
//	) {
//		try {
//			sysRoleService.deleteRoleWithPrivss(id);
//			return BaseResponse.success();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSROLE_DELETE_ERROR);
//		}
//	}
//
//	@JsonBodys({ @JsonBody(type = SysRole.class, excludes = { "createId", "creatorName", "createDate", "updateId",
//			"updaterName", "updateDate" }) })
//	// @Logable(value = "查询角色详情", path = "系统管理/角色管理/查询角色详情", message =
//	// "'根据角色ID（' + #id + '）查询角色详情信息'")
//	@Override
//	public BaseResponse<SysRole> infoById(//
//			@ApiParam(value = "角色ID", required = true) @RequestParam(required = true) Long id//
//	) {
//		try {
//			return BaseResponse.success(sysRoleService.selectExtensionPK(id));
//		} catch (ServiceException e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSROLE_QUERY_INFO_ERROR);
//		}
//	}
//
//}
