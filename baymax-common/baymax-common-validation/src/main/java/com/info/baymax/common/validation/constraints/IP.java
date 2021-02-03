package com.info.baymax.common.validation.constraints;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validate that the string is a IP address.
 *
 * @author jingwei.yang
 * @date 2021年2月2日 上午11:10:32
 */
@Documented
@Constraint(validatedBy = {IP.Validator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(IP.List.class)
public @interface IP {

    String message() default "{com.info.baymax.common.validation.constraints.IP.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @IP} annotations on the same element.
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        IP[] value();
    }

    class Validator implements ConstraintValidator<IP, String> {
        private final String regex = "^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$";

        @Override
        public boolean isValid(String ip, ConstraintValidatorContext constraintValidatorContext) {
            if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
                constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter("ip",
                    ip);
            }
            return ip != null && match(ip);
        }

        private boolean match(String ip) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(ip);
            try {
                if (!matcher.matches()) {
                    return false;
                } else {
                    for (int i = 1; i <= 4; i++) {
                        int octet = Integer.valueOf(matcher.group(i));
                        if (octet > 255) {
                            return false;
                        }
                    }
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

}
