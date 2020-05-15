package com.info.baymax.common.entity.validation;

import com.info.baymax.common.utils.CronExpression;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Cron.Validator.class)
public @interface Cron {

    String message() default "Not a valid crontab expression！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Cron, String> {
        @Override
        public boolean isValid(String cron, ConstraintValidatorContext context) {
            return cron != null && CronExpression.isValidExpression(cron);
        }
    }
}
