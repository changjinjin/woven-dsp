package com.info.baymax.common.validation.constraints;

import com.info.baymax.common.validation.constraints.EnumValue.List;
import com.info.baymax.common.validation.constraints.EnumValue.Validator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validate that the value is a Enum value.
 *
 * @author jingwei.yang
 * @date 2021年2月2日 上午11:10:32
 */
@Documented
@Constraint(validatedBy = {Validator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface EnumValue {

    String message() default "{com.info.baymax.common.validation.constraints.EnumValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类型
     *
     * @return 枚举类
     */
    Class<? extends Enum<?>> value();

    /**
     * 取值方法名称，为空的话默认取name()
     *
     * @return 取值方法名称
     */
    String valueMethod() default "";

    /**
     * 校验方法名称，默认返回bealean类型值，为空的话不执行，优先级高于valueMethod()
     *
     * @return 校验方法名称
     */
    String checkMethod() default "";

    /**
     * Defines several {@code @EnumValue} annotations on the same element.
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        EnumValue[] value();
    }

    class Validator implements ConstraintValidator<EnumValue, Object> {
        private Class<? extends Enum<?>> enumClass;
        private String valueMethod;

        private String checkMethod;

        @Override
        public void initialize(EnumValue enumValue) {
            enumClass = enumValue.value();
            valueMethod = enumValue.valueMethod();
            checkMethod = enumValue.checkMethod();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            if (value == null) {
                return true;
            }
            Enum<?>[] enumConstants = enumClass.getEnumConstants();

            if (StringUtils.isEmpty(checkMethod)) {
                Set<String> valueSet = null;
                if (StringUtils.isEmpty(valueMethod)) {
                    valueSet = Arrays.stream(enumConstants).map(t -> t.name()).collect(Collectors.toSet());
                } else {
                    valueSet = new HashSet<String>(enumConstants.length);

                    Method method;
                    try {
                        method = enumClass.getMethod(valueMethod);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(
                            String.format("%s method is not in the %s class", valueMethod, enumClass), e);
                    } catch (SecurityException e) {
                        throw new RuntimeException(String.format("%s method is not accessible method in the %s class",
                            valueMethod, enumClass), e);
                    }

                    Object invoke = null;
                    for (Enum<?> enum1 : enumConstants) {
                        try {
                            invoke = method.invoke(enum1);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                        if (invoke != null) {
                            valueSet.add(invoke.toString());
                        }
                    }
                }
                if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
                    constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)
                        .addMessageParameter("values", valueSet.toString());
                }
                return valueSet.contains(value);
            } else {
                Class<?> valueClass = value.getClass();
                try {
                    Method method = enumClass.getMethod(checkMethod, valueClass);
                    if (!Boolean.TYPE.equals(method.getReturnType()) && !Boolean.class.equals(method.getReturnType())) {
                        throw new RuntimeException(String.format("%s method return is not boolean type in the %s class",
                            checkMethod, enumClass));
                    }

                    if (!Modifier.isStatic(method.getModifiers())) {
                        throw new RuntimeException(String.format("%s method is not static method in the %s class",
                            checkMethod, enumClass));
                    }

                    Boolean result = (Boolean) method.invoke(null, value);
                    return result == null ? false : result;
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException | SecurityException e) {
                    throw new RuntimeException(String.format("This %s(%s) method does not exist in the %s",
                        checkMethod, valueClass, enumClass), e);
                }
            }
        }
    }
}