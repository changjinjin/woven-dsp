package com.info.baymax.common.core.annotation.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(ConditionalOnPropertyNotEmpty.OnPropertyNotEmptyCondition.class)
public @interface ConditionalOnPropertyNotEmpty {

	String value();

	class OnPropertyNotEmptyCondition implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			Map<String, Object> attrs = metadata.getAnnotationAttributes(ConditionalOnPropertyNotEmpty.class.getName());
			String propertyName = (String) attrs.get("value");
			Environment environment = context.getEnvironment();
			String val = environment.getProperty(propertyName);
			return val != null && !val.trim().isEmpty();
		}
	}
}
