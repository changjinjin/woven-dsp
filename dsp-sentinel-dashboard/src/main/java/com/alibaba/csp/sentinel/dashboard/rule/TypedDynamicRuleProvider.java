package com.alibaba.csp.sentinel.dashboard.rule;

import java.util.List;

import org.springframework.core.Ordered;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;

public interface TypedDynamicRuleProvider extends Supported, Ordered {

    /**
     * Fetch rules from remote rule configuration center for given application name.
     *
     * @param appName   app name
     * @param ruleClass rule entity class
     * @throws Exception if some error occurs
     */
    <T extends RuleEntity> List<T> getRules(String appName, Class<T> ruleClass, RuleType ruleType) throws Exception;

    @Override
    default int getOrder() {
        return 0;
    }
}
