package com.merce.woven.dsp.data.sys.crypto.pwd;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 密码信息
 *
 * @author jingwei.yang
 * @date 2019年9月24日 上午11:33:20
 */
@Data
@NoArgsConstructor
@ApiModel
public class PwdInfo implements Serializable {
    private static final long serialVersionUID = 7809909789618956073L;

    /**
     * 模式：SIMPLE/STRICT
     */
    @ApiModelProperty("模式：SIMPLE/STRICT")
    private PwdMode mode = PwdMode.SIMPLE;

    /**
     * 是否需要修改
     */
    @ApiModelProperty("是否需要修改")
    private boolean needChange;

    /**
     * 是否过期
     */
    @ApiModelProperty("是否过期")
    private boolean expired;

    /**
     * 是否是初始密码
     */
    @ApiModelProperty("是否是初始密码")
    private boolean inited;

    /**
     * 提示消息
     */
    @ApiModelProperty("提示消息")
    private String msg;

    public String getMsg() {
        if (inited) {
            return "你的密码是初始密码，为了账号安全请先修改密码。";
        }
        if (expired) {
            return "你的密码已过期，为了账号安全请先修改密码。";
        }
        return msg;
    }

    public boolean isNeedChange() {
        return isExpired() || isInited();
    }

    public PwdInfo(PwdMode mode, boolean expired, boolean inited) {
        this.mode = mode;
        this.expired = expired;
        this.inited = inited;
    }

}
