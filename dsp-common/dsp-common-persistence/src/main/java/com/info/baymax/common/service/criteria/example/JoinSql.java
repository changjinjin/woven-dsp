package com.info.baymax.common.service.criteria.example;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(doNotUseGetters = true)
public class JoinSql {

    @ApiModelProperty(value = "动态表名，可以是一个表名，也可以是一个子查询的结果表，如：(select * from user) u", hidden = true)
    private String dynamicTable;

    @ApiModelProperty(value = "追加表名，一个sql拼接片段端，如：t inner join user_role_ref urr on t.id = urr.user_id追加在（select * from t_user）之后用于关联条件用", hidden = true)
    private String appendTable;

    @ApiModelProperty(value = "表别名", hidden = true)
    private String tableAlias;
}
