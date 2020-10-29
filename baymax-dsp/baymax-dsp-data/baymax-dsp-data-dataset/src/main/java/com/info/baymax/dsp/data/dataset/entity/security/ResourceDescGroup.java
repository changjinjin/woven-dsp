package com.info.baymax.dsp.data.dataset.entity.security;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResourceDescGroup implements Serializable {
    private static final long serialVersionUID = -3618881429269900030L;

    @ApiModelProperty("资源目录类型：schema_dir，dataset_dir，datasource_dir，standard_dir，flow_dir，fileset_dir")
    private String resType;

    @ApiModelProperty("资源目录根节点信息")
    private ResourceDesc root;

}
