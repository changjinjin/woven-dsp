package com.alibaba.csp.sentinel.dashboard.rule;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSONArray;

public abstract class AbstractTypedDynamicRuleProvider implements TypedDynamicRuleProvider {

    @Override
    public <T extends RuleEntity> List<T> getRules(String appName, Class<T> ruleClass, RuleType ruleType)
        throws Exception {
        String rules = fetchFromRmote(appName, ruleClass, ruleType);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return JSONArray.parseArray(rules, ruleClass);
    }

    protected abstract <T> String fetchFromRmote(String appName, Class<T> ruleClass, RuleType ruleType)
        throws Exception;

}
