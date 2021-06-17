package com.info.baymax.dsp.access.dataapi.api;

import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class SqlRequest extends DataRequest<SqlQuery> {
    public static final long serialVersionUID = 945291461084587382L;

    public SqlRequest(String accessKey, Long dataServiceId, long timestamp, boolean encrypted, SqlQuery query) {
        super(accessKey, dataServiceId, timestamp, encrypted, query);
    }

    public SqlRequest(Long dataServiceId, long timestamp, boolean encrypted, SqlQuery query) {
        super("accessKey", dataServiceId, timestamp, encrypted, query);
    }
}
