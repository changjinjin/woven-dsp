package com.info.baymax.common.entity.validation;

import com.info.baymax.common.utils.CronExpression;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Cron.Validator.class)
public @interface Cron {

    String message() default "Not a valid crontab expression！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		Cron[] value();
	}

    class Validator implements ConstraintValidator<Cron, String> {
        @Override
        public boolean isValid(String cron, ConstraintValidatorContext context) {
            return cron != null && CronExpression.isValidExpression(cron);
        }
    }
    
}
