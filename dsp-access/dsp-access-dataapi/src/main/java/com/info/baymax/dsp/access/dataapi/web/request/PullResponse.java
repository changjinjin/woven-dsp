package com.info.baymax.dsp.access.dataapi.web.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.message.result.AbstractResponse;
import com.info.baymax.common.message.result.ErrMsg;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.utils.crypto.AESUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, doNotUseGetters = true, includeFieldNames = true)
@ApiModel
public class PullResponse extends AbstractResponse<Object> {
    private static final long serialVersionUID = 945291461084587382L;

    @ApiModelProperty("公共参数信息")
    private DataRequest<?> request;

    protected PullResponse(Integer status, String message) {
        this(status, message, null);
    }

    protected PullResponse(Integer status, String message, Object content) {
        super(status, message, content, null);
    }

    private String serialize() {
        try {
            ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
            return mapper.writeValueAsString(getContent());
        } catch (Exception e) {
            throw new RuntimeException("content serialize error", e);
        }
    }

    public PullResponse encrypt(String secretKey) {
        if (request != null && request.isEncrypted()) {
            content(AESUtil.encrypt(serialize(), secretKey));
        }
        return this;
    }

    public PullResponse request(DataRequest<?> request) {
        if (request != null) {
            request.setTimestamp(System.currentTimeMillis());
        }
        setRequest(request);
        return this;
    }

    public static PullResponse error(Integer status, String message) {
        return new PullResponse(status, message, null);
    }

    public static PullResponse error(ErrMsg errMsg) {
        return error(errMsg.getStatus(), errMsg.getMessage());
    }

    public static PullResponse error(ErrMsg errMsg, String customMessage) {
        return error(errMsg.getStatus(), StringUtils.defaultIfEmpty(customMessage, errMsg.getMessage()));
    }

    public PullResponse content(Object content) {
        this.content = content;
        return this;
    }

    public static PullResponse ok(Object content) {
        return error(ErrType.SUCCESS).content(content);
    }

}
