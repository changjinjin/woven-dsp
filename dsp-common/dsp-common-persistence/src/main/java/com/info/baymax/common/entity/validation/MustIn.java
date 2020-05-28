package com.info.baymax.common.entity.validation;

import com.info.baymax.common.entity.validation.MustIn.List;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.HashSet;
import java.util.Set;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(List.class)
@Constraint(validatedBy = {})
public @interface MustIn {

    String message() default "{javax.validation.constraints.MustIn.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        MustIn[] value();
    }

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
            // 不验空
            if (value == null) {
                return true;
            }
            if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
                constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)
                    .addMessageParameter("values", values.toString());
            }
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