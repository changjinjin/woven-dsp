package com.info.baymax.common.entity.handle;

import org.springframework.core.Ordered;

public interface EntityHandler extends Ordered {

	boolean supports(Object t);

	void handle(Object t);
}
