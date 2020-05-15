package com.info.baymax.common.comp.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenderConstraintValidator.class)
public @interface Gender {

    String message() default "Gender is Wrong!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
