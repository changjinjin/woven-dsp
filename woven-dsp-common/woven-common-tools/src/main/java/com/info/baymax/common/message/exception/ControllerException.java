package com.info.baymax.common.message.exception;

import com.info.baymax.common.message.result.ErrType;


/**
 *  Controller层异常
 * @author: jingwei.yang
 * @date:   2019年4月23日 下午2:55:00
 */
public class ControllerException extends BizException {
	private static final long serialVersionUID = -7428495813034319173L;

	public ControllerException(Integer status) {
		super(status);
	}

	public ControllerException(Integer status, String message) {
		super(status, message);
	}

	public ControllerException(ErrType type) {
		this(type.getStatus(), type.getMessage());
	}

	public ControllerException(ErrType type, String message) {
		this(type.getStatus(), message);
	}

	public ControllerException(Integer status, String message, Throwable cause) {
		super(status, message, cause);
	}

	public ControllerException(ServiceException e) {
		super(e.getStatus(), e.getMessage(), e.getCause());
	}

}
