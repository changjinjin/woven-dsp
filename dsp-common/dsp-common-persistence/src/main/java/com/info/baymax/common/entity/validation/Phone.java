package com.info.baymax.common.entity.validation;

import com.info.baymax.common.entity.validation.Phone.List;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(List.class)
@Constraint(validatedBy = Phone.Validator.class)
public @interface Phone {

    String message() default "{javax.validation.constraints.Phone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        Phone[] value();
    }

    class Validator implements ConstraintValidator<Phone, String> {
        private final String regex = "^1[3456789]\\d{9}$";

        @Override
        public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
            if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
                constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)
                    .addMessageParameter("phone", phone);
            }
            return phone != null && Pattern.matches(regex, phone);
        }
    }

}
