package com.info.baymax.dsp.access.dataapi.web.request;

import com.info.baymax.common.service.criteria.query.RecordQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class RecordRequest extends DataRequest<RecordQuery> {
    public static final long serialVersionUID = 945291461084587382L;

    public RecordRequest(String accessKey, Long dataServiceId, long timestamp, boolean encrypted, RecordQuery query) {
        super(accessKey, dataServiceId, timestamp, encrypted, query);
    } 
}