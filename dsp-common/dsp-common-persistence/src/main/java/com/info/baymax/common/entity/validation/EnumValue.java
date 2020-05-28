package com.info.baymax.common.entity.validation;

import com.info.baymax.common.entity.validation.EnumValue.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(List.class)
@Constraint(validatedBy = EnumValue.Validator.class)
public @interface EnumValue {

    String message() default "{javax.validation.constraints.EnumValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> enumClass();

    String enumMethod();

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        EnumValue[] value();
    }

    class Validator implements ConstraintValidator<EnumValue, Object> {
        private Class<? extends Enum<?>> enumClass;
        private String enumMethod;

        @Override
        public void initialize(EnumValue enumValue) {
            enumMethod = enumValue.enumMethod();
            enumClass = enumValue.enumClass();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            if (value == null) {
                return Boolean.TRUE;
            }
            if (enumClass == null || enumMethod == null) {
                return Boolean.TRUE;
            }
            Class<?> valueClass = value.getClass();
            try {
                Method method = enumClass.getMethod(enumMethod, valueClass);
                if (!Boolean.TYPE.equals(method.getReturnType()) && !Boolean.class.equals(method.getReturnType())) {
                    throw new RuntimeException(String.format("%s method return is not boolean type in the %s class",
                        enumMethod, enumClass));
                }

                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new RuntimeException(
                        String.format("%s method is not static method in the %s class", enumMethod, enumClass));
                }

                Boolean result = (Boolean) method.invoke(null, value);
                return result == null ? false : result;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(
                    String.format("This %s(%s) method does not exist in the %s", enumMethod, valueClass, enumClass),
                    e);
            }
        }
    }
}