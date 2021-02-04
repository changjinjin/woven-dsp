package com.info.baymax.common.validation.constraints;

import com.info.baymax.common.validation.passay.PasswordChecker;
import org.passay.PasswordData;
import org.passay.RuleResult;

import javax.annotation.Resource;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validate that the password string is compliant.
 *
 * @author jingwei.yang
 * @date 2021年2月2日 下午3:14:31
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Password.List.class)
@Constraint(validatedBy = Password.Validator.class)
public @interface Password {

    String message() default "{com.info.baymax.common.validation.constraints.Password.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Password[] value();
    }

    class Validator implements ConstraintValidator<Password, String> {
        @Resource
        private PasswordChecker passwordChecker;

        @Override
        public boolean isValid(String password, ConstraintValidatorContext context) {
            PasswordData passwordData = new PasswordData(password);
            RuleResult result = passwordChecker.validate(passwordData);
            if (result.isValid()) {
                return true;
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.join(",", passwordChecker.getMessages()))
                .addConstraintViolation();
            return false;
        }
    }
}
