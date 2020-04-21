package com.info.baymax.common.message.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 定义接口返回的数据包装
 *
 * @param <T> 报文主体内容
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:52:28
 */
@Setter
@Getter
@ApiModel
public class Response<T> implements Serializable {
	private static final long serialVersionUID = 3218508078738927801L;

	/**
	 * 业务状态码
	 */
	@ApiModelProperty("业务状态码：返回0表示请求处理成功，否则表示处理失败不是预期结果")
	private Integer status;

	/**
	 * 业务错误信息
	 */
	@ApiModelProperty("业务错误信息：status字段为非0值时返回对应的错误信息")
	private String message;

	/**
	 * 业务报文
	 */
	@ApiModelProperty("业务报文主体：当接口只需要返回简单状态时该节点为空")
	@JsonInclude(value = Include.NON_NULL)
	private T content;

	protected Response() {
	}

	protected Response(Integer status) {
		this.status = status;
	}

	protected Response(Integer status, String messge) {
		this.status = status;
		this.message = messge;
	}

	protected Response(Integer status, String message, T content) {
		this.status = status;
		this.message = message;
		this.content = content;
	}

	public boolean success() {
		return this.status != null && this.status.intValue() == 0;
	}

	private static <T> Response<T> execute(Integer status, String message, T content) {
		Response<T> r = new Response<>(status, message);
		if (content != null)
			r.setContent(content);
		return r;
	}

	private static <T> Response<T> execute(ErrType type, T content) {
		return execute(type.getStatus(), type.getMessage(), content);
	}

	private static <T> Response<T> execute(Integer status, String message) {
		return execute(status, message, null);
	}

	private static <T> Response<T> execute(ErrType type) {
		return execute(type.getStatus(), type.getMessage());
	}

	private static <T> Response<T> execute(Integer status) {
		return execute(status, ErrType.getMessageByStatus(status));
	}

	public static <T> Response<T> error(Integer status) {
		return execute(status);
	}

	public static <T> Response<T> error(Integer status, String message) {
		return execute(status, message);
	}

	public static <T> Response<T> error(ErrType type) {
		return execute(type);
	}

	public static <T> Response<T> error(ErrType type, String message) {
		return execute(type.getStatus(), message);
	}

	public static <T> Response<T> ok() {
		return execute(ErrType.SUCCESS);
	}

	public static <T> Response<T> ok(T content) {
		return execute(ErrType.SUCCESS, content);
	}

	public Response<T> status(Integer status) {
		setStatus(status);
		return this;
	}

	public Response<T> message(String message) {
		setMessage(message);
		return this;
	}

	public Response<T> content(T content) {
		setContent(content);
		return this;
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", message=" + message + ", content=" + content + "]";
	}
}
