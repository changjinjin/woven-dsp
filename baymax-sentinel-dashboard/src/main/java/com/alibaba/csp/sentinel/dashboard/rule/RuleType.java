package com.alibaba.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum RuleType {

    /**
     * flow.
     */
    FLOW("flow", FlowRule.class),
    /**
     * degrade.
     */
    DEGRADE("degrade", DegradeRule.class),
    /**
     * param flow.
     */
    PARAM_FLOW("param-flow", ParamFlowRule.class),
    /**
     * system.
     */
    SYSTEM("system", SystemRule.class),
    /**
     * authority.
     */
    AUTHORITY("authority", AuthorityRule.class),
    /**
     * gateway flow.
     */
    GW_FLOW("gw-flow",
        "com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule"),
    /**
     * api.
     */
    GW_API_GROUP("gw-api-group",
        "com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition");

    /**
     * alias for {@link AbstractRule}.
     */
    private final String name;

    /**
     * concrete {@link AbstractRule} class.
     */
    private Class clazz;

    /**
     * concrete {@link AbstractRule} class name.
     */
    private String clazzName;

    RuleType(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    RuleType(String name, String clazzName) {
        this.name = name;
        this.clazzName = clazzName;
    }

    public String getName() {
        return name;
    }

    public Class getClazz() {
        if (clazz != null) {
            return clazz;
        } else {
            try {
                return Class.forName(clazzName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Optional<RuleType> getByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return Optional.empty();
        }
        return Arrays.stream(RuleType.values())
            .filter(ruleType -> name.equals(ruleType.getName())).findFirst();
    }

    public static Optional<RuleType> getByClass(Class clazz) {
        return Arrays.stream(RuleType.values())
            .filter(ruleType -> clazz == ruleType.getClazz()).findFirst();
    }

}
