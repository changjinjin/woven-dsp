package com.info.baymax.common.queryapi.exception;

import com.info.baymax.common.queryapi.result.ErrType;

/**
 * Service 层异常
 * 
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:56:20
 */
public class ServiceException extends BizException {
	private static final long serialVersionUID = 1494875666602336146L;

	public ServiceException(Integer status) {
		super(status);
	}

	public ServiceException(Integer status, String message) {
		super(status, message);
	}

	public ServiceException(ErrType type) {
		this(type.getStatus(), type.getMessage());
	}

	public ServiceException(ErrType type, String message) {
		this(type.getStatus(), message);
	}

	public ServiceException(ErrType type, Throwable cause) {
		this(type.getStatus(), type.getMessage(), cause);
	}

	public ServiceException(Integer status, String message, Throwable cause) {
		super(status, message, cause);
	}

}
