package com.merce.woven.dsp.data.sys.entity.security;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RoleResourceRefGroup implements Serializable {
    private static final long serialVersionUID = -3741136847247699714L;

    @ApiModelProperty("资源类型")
    private String resType;

    @ApiModelProperty("角色资源关系")
    private List<RoleResourceRef> rrrs;
}