package com.info.baymax.common.entity.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface MustIn {

    String message() default "{custom.value.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value() default {};

    abstract class AbstractMustInValidator<T> implements ConstraintValidator<MustIn, T> {
        private Set<T> values;

        @Override
        public void initialize(MustIn mustIn) {
            values = new HashSet<T>();
            for (String value : mustIn.value()) {
                values.add(valueOf(value));
            }
        }

        protected abstract T valueOf(String value);

        @Override
        public boolean isValid(T value, ConstraintValidatorContext constraintValidatorContext) {
            return this.values.contains(value);
        }
    }

    class MustInValidatorForString extends AbstractMustInValidator<String> {
        @Override
        protected String valueOf(String value) {
            return value;
        }
    }

    class MustInValidatorForInteger extends AbstractMustInValidator<Integer> {
        @Override
        protected Integer valueOf(String value) {
            return Integer.valueOf(value);
        }
    }

    class MustInValidatorForLong extends AbstractMustInValidator<Long> {
        @Override
        protected Long valueOf(String value) {
            return Long.valueOf(value);
        }
    }

    class MustInValidatorForByte extends AbstractMustInValidator<Byte> {
        @Override
        protected Byte valueOf(String value) {
            return Byte.valueOf(value);
        }
    }

    class MustInValidatorForShort extends AbstractMustInValidator<Short> {
        @Override
        protected Short valueOf(String value) {
            return Short.valueOf(value);
        }
    }

    class MustInValidatorForFloat extends AbstractMustInValidator<Float> {
        @Override
        protected Float valueOf(String value) {
            return Float.valueOf(value);
        }
    }

    class MustInValidatorForDouble extends AbstractMustInValidator<Double> {
        @Override
        protected Double valueOf(String value) {
            return Double.valueOf(value);
        }
    }

    class MustInValidatorForBoolean extends AbstractMustInValidator<Boolean> {
        @Override
        protected Boolean valueOf(String value) {
            return Boolean.valueOf(value);
        }
    }
}