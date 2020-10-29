package com.alibaba.csp.sentinel.dashboard.controller.v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.repository.rule.InMemoryRuleRepositoryAdapter;
import com.alibaba.csp.sentinel.dashboard.rule.CompositeRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.CompositeRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.RuleType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RuleController<T extends RuleEntity> {

    @Autowired
    protected AppManagement appManagement;
    @Autowired
    protected CompositeRuleProvider ruleProvider;
    @Autowired
    protected CompositeRulePublisher rulePublisher;
    @Autowired
    protected InMemoryRuleRepositoryAdapter<T> repository;

    protected boolean publishRules(String app, RuleType ruleType) {
        try {
            List<T> rules = repository.findAllByApp(app);
            rulePublisher.publish(app, rules, ruleType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

}
