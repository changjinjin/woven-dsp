package com.info.baymax.common.persistence.jpa.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Deprecated
public class Pageable implements Serializable {
    private static final long serialVersionUID = 4460131260956215413L;

    @ApiModelProperty("页码")
    private int page;
    @ApiModelProperty("页长")
    private int limit;
}
