package com.info.baymax.dsp.access.dataapi.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PullResponse<T> implements Serializable {
	private static final long serialVersionUID = 945291461084587382L;

	private List<T> list;
}
