//package com.jusfoun.services.auth.server.web.controller;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jusfoun.common.base.controller.BaseIdableController;
//import com.jusfoun.common.base.service.BaseIdableService;
//import com.jusfoun.common.base.service.BaseService;
//import com.jusfoun.common.enums.types.YesNoType;
//import com.jusfoun.common.message.exception.ControllerException;
//import com.jusfoun.common.message.result.BaseResponse;
//import com.jusfoun.common.message.result.ErrType;
//import com.jusfoun.services.auth.api.entity.SysUser;
//import com.jusfoun.services.auth.server.config.InitConfig;
//import com.jusfoun.services.auth.server.service.SysUserService;
//import com.jusfoun.services.ops.api.enums.AccountStatus;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//
///**
// * 说明： 系统用户管理接口. <br>
// *
// * @author yjw@jusfoun.com
// * @date 2017年9月23日 上午11:45:00
// */
//@Api(tags = "用户管理", value = "系统用户管理", description = "系统用户管理接口类")
//@RestController
//@RequestMapping(value = { "/v1/sysuser", "/app/sysuser" })
//@ConditionalOnBean({ PasswordEncoder.class })
//public class SysUserController implements BaseIdableController<SysUser, Long> {
//
//	@Autowired
//	private SysUserService sysUserService;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Autowired
//	private InitConfig initConfig;
//
//	@Override
//	public BaseIdableService<SysUser> getBaseIdableService() {
//		return sysUserService;
//	}
//
//	@Override
//	public BaseService<SysUser> getBaseService() {
//		return sysUserService;
//	}
//
//	// @Logable(value = "保存系统用户", path = "系统管理/用户管理/保存系统用户")
//	@Override
//	public BaseResponse<SysUser> save(//
//			@RequestBody SysUser t//
//	) {
//		if (t == null) {
//			return BaseResponse.fail(ErrType.BAD_REQUEST);
//		}
//		if (StringUtils.isEmpty(t.getUsername())) {
//			return BaseResponse.fail(ErrType.BAD_REQUEST, "用户名称不能为空");
//		}
//
//		// 检查数据是否已经存在
//		SysUser record = new SysUser();
//		record.setUsername(t.getUsername());
//		int count = sysUserService.selectCount(record);
//		if (count > 0) {
//			return BaseResponse.fail(ErrType.SYSUSER_ENTITY_EXIST_ERROR, "系统已存在同名的账户");
//		}
//
//		// 保存数据
//		try {
//			t.setPassword(
//					passwordEncoder.encode(StringUtils.defaultIfEmpty(t.getPassword(), initConfig.getPassword())));
//			if (t.getStatus() == null) {
//				t.setStatus(AccountStatus.NOT_ENABLED.getValue());
//			}
//			if (t.getGender() == null) {
//				t.setGender(YesNoType.NO.getValue());
//			}
//			sysUserService.insertWithCache(t);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSUSER_SAVE_ERROR);
//		}
//		return BaseResponse.success();
//	}
//
//	// @Logable(value = "修改系统用户", path = "系统管理/用户管理/修改系统用户")
//	@Override
//	public BaseResponse<SysUser> updateById(//
//			@RequestBody SysUser t//
//	) {
//		if (t == null || t.getId() == null) {
//			return BaseResponse.fail(ErrType.BAD_REQUEST);
//		}
//		// 更新数据
//		try {
//			sysUserService.updateByPrimaryKeySelectiveWithCache(t);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSUSER_UPDATE_ERROR);
//		}
//		return BaseResponse.success();
//	}
//
//	// @Logable(value = "删除系统用户", path = "系统管理/用户管理/删除系统用户")
//	@Override
//	public BaseResponse<?> deleteById(//
//			@ApiParam(value = "用户ID", required = true) @RequestParam Long id//
//	) {
//		try {
//			sysUserService.deleteWithRoles(id);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSUSER_DELETE_ERROR);
//		}
//		return BaseResponse.success();
//	}
//
//	// @Logable(value = "查询系统用户详情", path = "系统管理/用户管理/查询系统用户详情")
//	@Override
//	public BaseResponse<SysUser> infoById(//
//			@ApiParam(value = "用户ID", required = true) @RequestParam Long id//
//	) {
//		try {
//			SysUser sysUser = sysUserService.selectPKWithCache(id);
//			sysUser.setPassword(null);
//			return BaseResponse.success(sysUser);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSUSER_QUERY_INFO_ERROR);
//		}
//	}
//
//	@ApiOperation(value = "重置用户密码", notes = "重置用户密码", hidden = false)
//	// @Logable(value = "重置用户密码", path = "系统管理/用户管理/重置用户密码")
//	@RequestMapping(value = "/resetPass", method = { RequestMethod.POST, RequestMethod.GET })
//	public BaseResponse<?> resetPass(//
//			@ApiParam(value = "重置密码的记录ID", required = true) @RequestParam Long id//
//	) {
//		try {
//			SysUser sysUser = new SysUser();
//			sysUser.setId(id);
//			sysUser.setPassword(passwordEncoder.encode(initConfig.getPassword()));
//			sysUserService.updateByPrimaryKeySelectiveWithCache(sysUser);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSUSER_UPDATE_PASSWORD_ERROR);
//		}
//		return BaseResponse.success();
//	}
//
//	/*
//	 * @ApiOperation(value = "修改用户密码", notes = "修改用户密码", hidden = false)
//	 * // @Logable(value = "修改用户密码", path = "系统管理/用户管理/修改用户密码")
//	 * 
//	 * @PostMapping("/modifyPass") public BaseResponse<?> modifyPass(//
//	 * 
//	 * @ApiParam(value = "原密码", required = true) @RequestParam String
//	 * oldPassword, //
//	 * 
//	 * @ApiParam(value = "新密码", required = true) @RequestParam String password
//	 * // ) { try { String username = SecurityUtils.getCurrentUserUsername();
//	 * SysUser user = sysUserService.selectByUsername(username);
//	 * 
//	 * if (!passwordEncoder.matches(oldPassword, user.getPassword())) { return
//	 * BaseResponse.fail(ErrType.SYSUSER_AUTH_PASSWORD_ERROR); }
//	 * user.setPassword(passwordEncoder.encode(password));
//	 * sysUserService.updateByPrimaryKeySelectiveWithCache(user); } catch
//	 * (Exception e) { e.printStackTrace(); throw new
//	 * ControllerException(ErrType.SYSUSER_UPDATE_PASSWORD_ERROR); } return
//	 * BaseResponse.success(); }
//	 */
//
//	@ApiOperation(value = "修改用户角色", notes = "修改用户角色", hidden = false)
//	// @Logable(value = "修改用户角色", path = "系统管理/用户管理/修改用户角色")
//	@PostMapping("/modifyRoles")
//	public BaseResponse<?> modifyRoles(//
//			@ApiParam(value = "用户信息，包含用户的角色信息", required = true) @RequestBody SysUser sysUser//
//	) {
//		if (sysUser == null || sysUser.getId() == null) {
//			return BaseResponse.fail(ErrType.BAD_REQUEST);
//		}
//		try {
//			sysUserService.modifyRoles(sysUser);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new ControllerException(ErrType.SYSUSER_UPDATE_PASSWORD_ERROR);
//		}
//		return BaseResponse.success();
//	}
//}
