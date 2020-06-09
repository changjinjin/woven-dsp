package com.alibaba.csp.sentinel.dashboard.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;

@Component
public class CompositeRulePublisher extends DsTypeService
    implements TypedDynamicRulePublisher, ApplicationContextAware {
    private List<TypedDynamicRulePublisher> delegates = new ArrayList<TypedDynamicRulePublisher>();

    @Override
    public <T extends RuleEntity> void publish(String appName, List<T> rules, RuleType ruleType) throws Exception {
        for (TypedDynamicRulePublisher publisher : delegates) {
            if (publisher.supports(dsType())) {
                publisher.publish(appName, rules, ruleType);
                return;
            }
        }
        throw new RuleException("No proper Rule Publisher!");
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        Map<String, TypedDynamicRulePublisher> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
            TypedDynamicRulePublisher.class, true, false);
        this.delegates = new ArrayList<TypedDynamicRulePublisher>(beans.values());
        excludeSelfAndOrderHandlers();
    }

    private void excludeSelfAndOrderHandlers() {
        // 排除本身,避免调用时出现死循环
        this.delegates = this.delegates.stream().filter(t -> !(t instanceof CompositeRulePublisher))
            .collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(this.delegates);
    }
}
