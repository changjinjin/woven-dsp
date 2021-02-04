package com.info.baymax.security.oauth.api.exception;

import com.info.baymax.common.core.result.ErrMsg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@Getter
@AllArgsConstructor
public enum OAuth2ErrMsg implements ErrMsg {
    OAUTH2_ERROR(4001, OAuth2Exception.ERROR), //
    OAUTH2_ERROR_DESCRIPTION(4002, OAuth2Exception.DESCRIPTION), //
    OAUTH2_ERROR_URI(4003, OAuth2Exception.URI), //
    OAUTH2_INVALID_REQUEST(4004, OAuth2Exception.INVALID_REQUEST), //
    OAUTH2_INVALID_CLIENT(4005, OAuth2Exception.INVALID_CLIENT), //
    OAUTH2_INVALID_GRANT(4006, OAuth2Exception.INVALID_GRANT), //
    OAUTH2_UNAUTHORIZED_CLIENT(4007, OAuth2Exception.UNAUTHORIZED_CLIENT), //
    OAUTH2_UNSUPPORTED_GRANT_TYPE(4008, OAuth2Exception.UNSUPPORTED_GRANT_TYPE), //
    OAUTH2_INVALID_SCOPE(4009, OAuth2Exception.INVALID_SCOPE), //
    OAUTH2_INSUFFICIENT_SCOPE(4010, OAuth2Exception.INSUFFICIENT_SCOPE), //
    OAUTH2_INVALID_TOKEN(4011, OAuth2Exception.INVALID_TOKEN), //
    OAUTH2_REDIRECT_URI_MISMATCH(4012, OAuth2Exception.REDIRECT_URI_MISMATCH), //
    OAUTH2_UNSUPPORTED_RESPONSE_TYPE(4013, OAuth2Exception.UNSUPPORTED_RESPONSE_TYPE), //
    OAUTH2_ACCESS_DENIED(4014, OAuth2Exception.ACCESS_DENIED);//

    private final Integer status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}