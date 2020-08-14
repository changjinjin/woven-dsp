package com.info.baymax.access.dataapi.api;

import com.info.baymax.common.queryapi.query.aggregate.AggQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AggRequest extends DataRequest<AggQuery> {
    public static final long serialVersionUID = 945291461084587382L;

    public AggRequest(String accessKey, Long dataServiceId, long timestamp, boolean encrypted, AggQuery query) {
        super(accessKey, dataServiceId, timestamp, encrypted, query);
    }
}
