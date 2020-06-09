package com.alibaba.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.slots.block.Rule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;

/**
 * 规则类型
 *
 * @author jingwei.yang
 * @date 2020年6月8日 下午2:37:15
 */
public enum RuleType {

    flow(FlowRule.class, FlowRuleEntity.class), degrade(DegradeRule.class, DegradeRuleEntity.class),
    paramflow(ParamFlowRule.class, ParamFlowRuleEntity.class), system(SystemRule.class, SystemRuleEntity.class),
    authority(AuthorityRule.class, AuthorityRuleEntity.class);

    private final Class<? extends Rule> ruleClass;
    private final Class<? extends RuleEntity> ruleEntityClass;

    private RuleType(Class<? extends Rule> ruleClass, Class<? extends RuleEntity> ruleEntityClass) {
        this.ruleClass = ruleClass;
        this.ruleEntityClass = ruleEntityClass;
    }

    public Class<? extends Rule> getRuleClass() {
        return ruleClass;
    }

    public Class<? extends RuleEntity> getRuleEntityClass() {
        return ruleEntityClass;
    }

}
