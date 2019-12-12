package com.merce.woven.common.entity.id;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Integer类型主键
 *
 * @author jingwei.yang
 * @date 2019-05-29 16:00
 */
public class IntegerIdable implements Idable<Integer> {
    private static final long serialVersionUID = 9126190618948914748L;

    @ApiModelProperty("主键")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
