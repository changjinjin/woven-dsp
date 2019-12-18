package com.info.baymax.dsp.auth.api.exception;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CustomOauth2ExceptionSerializer.class)
public class CustomOauth2Exception extends OAuth2Exception {
	private static final long serialVersionUID = -8871557007209001038L;

	private String oAuth2ErrorCode;
	private int httpErrorCode;
	private Map<String, String> additionalInformation;

	public CustomOauth2Exception(String msg) {
		super(msg);
	}

	public CustomOauth2Exception(String oAuth2ErrorCode, int httpErrorCode, Map<String, String> additionalInformation,
			String msg) {
		super(msg);
		this.oAuth2ErrorCode = oAuth2ErrorCode;
		this.httpErrorCode = httpErrorCode;
		this.additionalInformation = additionalInformation;
	}

	public CustomOauth2Exception(OAuth2Exception e) {
		this(e.getOAuth2ErrorCode(), e.getHttpErrorCode(), e.getAdditionalInformation(), e.getMessage());
	}

	@Override
	public String getOAuth2ErrorCode() {
		return oAuth2ErrorCode;
	}

	@Override
	public int getHttpErrorCode() {
		return httpErrorCode;
	}

	@Override
	public Map<String, String> getAdditionalInformation() {
		return additionalInformation;
	}

	public String getSummary() {

		StringBuilder builder = new StringBuilder();

		String delim = "";

		String error = this.getOAuth2ErrorCode();
		if (error != null) {
			builder.append(StringUtils.defaultString(getErrorPrefix(error) + ":", ""));
		}

		String errorMessage = this.getMessage();
		if (errorMessage != null) {
			builder.append(errorMessage);
			delim = ", ";
		}

		Map<String, String> additionalParams = this.getAdditionalInformation();
		if (additionalParams != null) {
			for (Map.Entry<String, String> param : additionalParams.entrySet()) {
				builder.append(delim).append(param.getKey()).append("=\"").append(param.getValue()).append("\"");
				delim = ", ";
			}
		}
		return builder.toString();

	}

	public String getErrorPrefix(String errorCode) {
		if (INVALID_CLIENT.equals(errorCode)) {
			return "无效的客户端";
		} else if (UNAUTHORIZED_CLIENT.equals(errorCode)) {
			return "未授权的客户端";
		} else if (INVALID_GRANT.equals(errorCode)) {
			return "授权无效";
		} else if (INVALID_SCOPE.equals(errorCode)) {
			return "无效的授权范围";
		} else if (INVALID_TOKEN.equals(errorCode)) {
			return "无效的令牌";
		} else if (INVALID_REQUEST.equals(errorCode)) {
			return "无效的请求参数";
		} else if (REDIRECT_URI_MISMATCH.equals(errorCode)) {
			return "重定向地址无效";
		} else if (UNSUPPORTED_GRANT_TYPE.equals(errorCode)) {
			return "不支持的授权类型";
		} else if (UNSUPPORTED_RESPONSE_TYPE.equals(errorCode)) {
			return "不支持的响应类型";
		} else if (ACCESS_DENIED.equals(errorCode)) {
			return "拒绝访问";
		} else {
			return null;
		}
	}

}
