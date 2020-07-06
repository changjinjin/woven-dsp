package com.info.baymax.dsp.access.dataapi.web.request;

import com.info.baymax.dsp.access.dataapi.data.RecordQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class PullRequest extends DataRequest<RecordQuery> {
    public static final long serialVersionUID = 945291461084587382L;

    public PullRequest(String accessKey, Long dataServiceId, long timestamp, boolean encrypted, RecordQuery query) {
        super(accessKey, dataServiceId, timestamp, encrypted, query);
    } 
}
