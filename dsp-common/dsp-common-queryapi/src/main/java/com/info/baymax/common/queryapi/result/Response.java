package com.info.baymax.common.queryapi.result;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.ToString;

@ApiModel
@Getter
@ToString
public class Response<T> extends AbstractResponse<T> {
	private static final long serialVersionUID = -5597707689816894840L;

	protected Response(ErrMsg errMsg, T content) {
		this(errMsg.getStatus(), errMsg.getMessage(), content);
	}

	protected Response(Integer status, String message, T content) {
		super(status, message, content, null);
	}

	protected Response(Integer status, String message, T content, Object details) {
		super(status, message, content, details);
	}

	// Static builder methods
	public static Builder status(int status) {
		return new DefaultBuilder(status);
	}

	public static Builder error(ErrMsg errMsg) {
		return status(errMsg.getStatus()).message(errMsg.getMessage());
	}

	public static Builder error(int status, String customMessage) {
		return status(status).message(customMessage);
	}

	public static Builder error(ErrMsg errMsg, String customMessage) {
		return status(errMsg.getStatus())
				.message((customMessage != null && !customMessage.isEmpty()) ? customMessage : errMsg.getMessage());
	}

	public static Builder ok() {
		return error(ErrType.SUCCESS);
	}

	public static <T> Response<T> ok(T content) {
		return ok().content(content);
	}

	public static Builder badRequest() {
		return error(ErrType.BAD_REQUEST);
	}

	public static Builder unauthorized() {
		return error(ErrType.UNAUTHORIZED);
	}

	public static Builder forbidden() {
		return error(ErrType.FORBIDDEN);
	}

	public static Builder notFound() {
		return error(ErrType.NOT_FOUND);
	}

	public static Builder methodNotAllowed() {
		return error(ErrType.METHOD_NOT_ALLOWED);
	}

	public static Builder notAcceptable() {
		return error(ErrType.NOT_ACCEPTABLE);
	}

	public static Builder requestTimeout() {
		return error(ErrType.REQUEST_TIMEOUT);
	}

	public static Builder internalServerError() {
		return error(ErrType.INTERNAL_SERVER_ERROR);
	}

	public static Builder badGateway() {
		return error(ErrType.BAD_GATEWAY);
	}

	public static Builder serviceUnavailable() {
		return error(ErrType.SERVICE_UNAVAILABLE);
	}

	public static Builder gatewayTimeout() {
		return error(ErrType.GATEWAY_TIMEOUT);
	}

	public interface Builder {

		Builder message(String message);

		Builder details(Object details);

		default <T> Response<T> build() {
			return content(null);
		}

		<T> Response<T> content(T content);
	}

	private static class DefaultBuilder implements Builder {

		private final Integer status;
		private String message;
		private Object details;

		public DefaultBuilder(Integer status) {
			this.status = status;
		}

		@Override
		public Builder message(String message) {
			this.message = message;
			return this;
		}

		@Override
		public Builder details(Object details) {
			this.details = details;
			return this;
		}

		@Override
		public <T> Response<T> content(T content) {
			return new Response<T>(this.status, this.message, content, this.details);
		}
	}
}
