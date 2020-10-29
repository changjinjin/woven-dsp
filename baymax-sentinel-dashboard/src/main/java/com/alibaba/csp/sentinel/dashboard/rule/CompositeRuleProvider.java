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
public class CompositeRuleProvider extends DsTypeService implements TypedDynamicRuleProvider, ApplicationContextAware {
    private List<TypedDynamicRuleProvider> delegates = new ArrayList<TypedDynamicRuleProvider>();

    @Override
    public <T extends RuleEntity> List<T> getRules(String appName, Class<T> ruleClass, RuleType ruleType)
        throws Exception {
        for (TypedDynamicRuleProvider provider : delegates) {
            if (provider.supports(dsType())) {
                return provider.getRules(appName, ruleClass, ruleType);
            }
        }
        throw new RuleException("No proper processor job handler to handle MonitorRule.");
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        Map<String, TypedDynamicRuleProvider> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
            TypedDynamicRuleProvider.class, true, false);
        this.delegates = new ArrayList<TypedDynamicRuleProvider>(beans.values());
        excludeSelfAndOrderHandlers();
    }

    private void excludeSelfAndOrderHandlers() {
        // 排除本身,避免调用时出现死循环
        this.delegates = this.delegates.stream().filter(t -> !(t instanceof CompositeRuleProvider))
            .collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(this.delegates);
    }
}
