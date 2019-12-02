package com.merce.woven.dsp.auth.server.oauth2;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 说明： 登录请求包装. <br>
 * 
 * @author yjw@jusfoun.com
 * @date 2017年11月8日 下午2:53:15
 */
@ApiModel
public class LoginRequest implements Serializable{
	private static final long serialVersionUID = 2033676172425728786L;

	@ApiModelProperty("用户名")
	private String grant_type;

	@ApiModelProperty("用户名")
	private String username;

	@ApiModelProperty("用户密码")
	private String password;

	@JsonCreator
	public LoginRequest(@JsonProperty("grant_type") String grant_type, @JsonProperty("username") String username, @JsonProperty("password") String password) {
		this.grant_type = grant_type;
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
