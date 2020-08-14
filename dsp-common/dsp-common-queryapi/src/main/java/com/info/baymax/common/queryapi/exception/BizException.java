package com.info.baymax.common.queryapi.exception;

import com.info.baymax.common.queryapi.result.ErrType;

/**
 * 定义业务异常
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:55:39
 */
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 状态码
	 */
	private Integer status;

	/**
	 * 异常信息
	 */
	private String message;

	public BizException() {
	}

	public BizException(Integer status) {
		this(status, ErrType.getMessageByStatus(status));
	}

	public BizException(Integer status, String message) {
		this.status = status;
		this.message = message;
	}

	public BizException(ErrType type) {
		this(type.getStatus(), type.getMessage());
	}

	public BizException(ErrType type, String message) {
		this(type.getStatus(), message);
	}

	public BizException(Integer status, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

}
