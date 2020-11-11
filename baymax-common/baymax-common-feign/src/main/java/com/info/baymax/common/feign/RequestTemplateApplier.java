package com.info.baymax.common.feign;

import feign.RequestTemplate;

public interface RequestTemplateApplier {
    void apply(RequestTemplate requestTemplate);
}
