package com.info.baymax.dsp.access.dataapi.web.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.message.result.Response;
import com.info.baymax.common.utils.crypto.AESUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, doNotUseGetters = true, includeFieldNames = true)
@ApiModel
public class PullResponse extends Response<Object> {
    private static final long serialVersionUID = 945291461084587382L;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    private ObjectMapper mapper;

    @ApiModelProperty("公共参数信息")
    private DataRequest request;

    protected PullResponse() {
    }

    protected PullResponse(Integer status) {
        super(status);
    }

    protected PullResponse(Integer status, String messge) {
        super(status, messge);
    }

    protected PullResponse(Integer status, String message, Object content) {
        super(status, message, content);
    }

    private String serialize() {
        Object content = getContent();
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        try {
            return mapper.writeValueAsString(content);
        } catch (Exception e) {
            throw new RuntimeException("content serialize error", e);
        }
    }

    public PullResponse encrypt(String secretKey) {
        if (request != null && request.isEncrypted()) {
            setContent(AESUtil.encrypt(serialize(), secretKey));
        }
        return this;
    }

    private static PullResponse execute(Integer status, String message, Object content) {
        PullResponse r = new PullResponse(status, message);
        if (content != null)
            r.setContent(content);
        return r;
    }

    private static PullResponse execute(ErrType type, Object content) {
        return execute(type.getStatus(), type.getMessage(), content);
    }

    private static PullResponse execute(Integer status, String message) {
        return execute(status, message, null);
    }

    private static PullResponse execute(ErrType type) {
        return execute(type.getStatus(), type.getMessage());
    }

    private static PullResponse execute(Integer status) {
        return execute(status, ErrType.getMessageByStatus(status));
    }

    public static PullResponse error(Integer status) {
        return execute(status);
    }

    public static PullResponse error(Integer status, String message) {
        return execute(status, message);
    }

    public static PullResponse error(ErrType type) {
        return execute(type);
    }

    public static PullResponse error(ErrType type, String message) {
        return execute(type.getStatus(), message);
    }

    public static PullResponse ok() {
        return execute(ErrType.SUCCESS);
    }

    public static PullResponse ok(Object content) {
        return execute(ErrType.SUCCESS, content);
    }

    public PullResponse status(Integer status) {
        setStatus(status);
        return this;
    }

    public PullResponse message(String message) {
        setMessage(message);
        return this;
    }

    public PullResponse content(Object content) {
        setContent(content);
        return this;
    }

    public PullResponse request(DataRequest request) {
        if (request != null) {
            request.setTimestamp(System.currentTimeMillis());
        }
        setRequest(request);
        return this;
    }

}
