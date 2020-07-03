package com.info.baymax.dsp.access.dataapi.web.request;

import com.info.baymax.common.service.criteria.agg.AggQuery;
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
