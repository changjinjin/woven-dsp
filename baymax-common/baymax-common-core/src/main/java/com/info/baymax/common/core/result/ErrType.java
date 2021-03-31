package com.info.baymax.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务异常枚举类
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:54:23
 */
@Getter
@AllArgsConstructor
public enum ErrType implements ErrMsg {

    /*********************** 基础错误 ********************** */
    SUCCESS(0, "Request successfully."), //
    FAILED(1, "Request failed."), //

    /*********************** 常用http请求错误 ********************** */
    REQUEST_OK(200, "OK"), //
    BAD_REQUEST(400, "Bad Request."), //
    UNAUTHORIZED(401, "Unauthorized."), //
    FORBIDDEN(403, "Forbidden."), //
    NOT_FOUND(404, "Not Found."), //
    METHOD_NOT_ALLOWED(405, "Method Not Allowed."), //
    NOT_ACCEPTABLE(406, "Not Acceptable."), //
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required."), //
    REQUEST_TIMEOUT(408, "Request Timeout."), //
    CONFLICT(409, "Conflict."), //
    GONE(410, "Gone."), //
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type."), //
    INTERNAL_SERVER_ERROR(500, "Internal Server Error."), //
    NOT_IMPLEMENTED(501, "Not Implemented."), //
    BAD_GATEWAY(502, "Bad Gateway."), //
    SERVICE_UNAVAILABLE(503, "Service Unavailable."), //
    GATEWAY_TIMEOUT(504, "Gateway Timeout."),

    /*********************** 通用实体错误 ********************** */
    ENTITY_EMPTY(1001, "Entity is empty."), //
    ENTITY_EXIST(1002, "Entity is exist."), //
    ENTITY_NOT_EXIST(1003, "Entity not exist."), //
    ENTITY_SAVE_ERROR(1004, "Entity save failed."), //
    ENTITY_UPDATE_ERROR(1005, "Entity update failed."), //
    ENTITY_DELETE_ERROR(1006, "Entity delete failed."), //
    ENTITY_QUERY_LIST_ERROR(1007, "Failed to query data list."), //
    ENTITY_QUERY_INFO_ERROR(1008, "Failed to query data details."), //
    ENTITY_DUPLICATE_KEY(1009, "Entity duplicate key."), //
    ENTITY_DUPLICATE_NAME(1010, "Entity duplicate name."), //
    ENTITY_UNIQUE_CONSTRAINT(1011, "Violation of unique constraint."), //

    FILE_MAX_UPLOAD_SIZE_EXCEEDED_ERROR(1761, "File is too large."), //
    FILE_FORMAT_NOT_ALLOW_ERROR(1762, "File format error."), //
    FILE_IO_WRITE_ERROR(1763, "File write failed."), //
    FILE_IO_READ_ERROR(1764, "File write failed."), //
    FILE_DELETE_ERROR(1765, "File delete failed."), //
    FILE_NOT_FOUND_ERROR(1766, "File not found."), //
    FILE_EXPORT_FAILED(1767, "File export failed"), //

    SECRET_KEY_ERROR(1801, "Secret key error"), //
    SECRET_KEY_EXPIRED(1802, "Secret key expired") //
    ;
    private final Integer status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }

}
