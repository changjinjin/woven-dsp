package com.info.baymax.security.oauth.api.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.info.baymax.security.oauth.api.i18n.Oauth2MessageSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.util.Map;

@JsonSerialize(using = CustomOauth2ExceptionSerializer.class)
public class CustomOauth2Exception extends OAuth2Exception {
    private static final long serialVersionUID = -8871557007209001038L;
    private MessageSourceAccessor accessor = Oauth2MessageSource.getAccessor();

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
        switch (errorCode) {
            case INVALID_CLIENT:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_INVALID_CLIENT.name());
            case UNAUTHORIZED_CLIENT:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_UNAUTHORIZED_CLIENT.name());
            case INVALID_GRANT:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_INVALID_GRANT.name());
            case INVALID_SCOPE:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_INVALID_SCOPE.name());
            case INVALID_TOKEN:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_INVALID_TOKEN.name());
            case INVALID_REQUEST:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_INVALID_REQUEST.name());
            case REDIRECT_URI_MISMATCH:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_REDIRECT_URI_MISMATCH.name());
            case UNSUPPORTED_GRANT_TYPE:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_UNSUPPORTED_GRANT_TYPE.name());
            case UNSUPPORTED_RESPONSE_TYPE:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_UNSUPPORTED_RESPONSE_TYPE.name());
            case ACCESS_DENIED:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_ACCESS_DENIED.name());
            default:
                return accessor.getMessage(OAuth2ErrMsg.OAUTH2_ERROR.name());
        }
    }

}
