package com.info.baymax.dsp.access.dataapi.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PullRequest implements Serializable {
	private static final long serialVersionUID = 945291461084587382L;

	private String dataServiceId;
	private String accessKey;
	private int offset;
	private int size;
}
