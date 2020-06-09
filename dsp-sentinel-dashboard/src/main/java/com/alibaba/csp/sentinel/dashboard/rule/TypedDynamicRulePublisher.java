package com.alibaba.csp.sentinel.dashboard.rule;

import java.util.List;

import org.springframework.core.Ordered;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;

public interface TypedDynamicRulePublisher extends Supported, Ordered {

    /**
     * Publish rules to remote rule configuration center for given application name.
     *
     * @param appName  app name
     * @param rules    list of rules to push
     * @param ruleType rule type
     * @throws Exception if some error occurs
     */
    <T extends RuleEntity> void publish(String appName, List<T> rules, RuleType ruleType) throws Exception;

    @Override
    default int getOrder() {
        return 0;
    }

}
