package com.info.baymax.common.validation.constraints;

import com.info.baymax.common.utils.CronExpression;
import com.info.baymax.common.validation.constraints.Cron.List;
import com.info.baymax.common.validation.constraints.Cron.Validator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validate that the string is a Cron expression.
 *
 * @author jingwei.yang
 * @date 2021年2月2日 上午11:10:32
 */
@Documented
@Constraint(validatedBy = {Validator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface Cron {

    String message() default "{com.info.baymax.common.validation.constraints.Cron.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @Cron} annotations on the same element.
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        Cron[] value();
    }

    class Validator implements ConstraintValidator<Cron, String> {
        @Override
        public boolean isValid(String cron, ConstraintValidatorContext constraintValidatorContext) {
            if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
                constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter("cron",
                    cron);
            }
            return cron != null && CronExpression.isValidExpression(cron);
        }
    }

}
