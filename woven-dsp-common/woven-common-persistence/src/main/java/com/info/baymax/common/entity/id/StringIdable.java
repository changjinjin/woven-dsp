package com.info.baymax.common.entity.id;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;


/**
 * String类型主键
 *
 * @author jingwei.yang
 * @date 2019-05-29 16:00
 */
public class StringIdable implements Idable<String> {
    private static final long serialVersionUID = 9126190618948914748L;

    @ApiModelProperty("主键")
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
