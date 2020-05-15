package com.info.baymax.common.comp.valid;

import com.info.baymax.common.utils.CronExpression;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CronConstraintValidator implements ConstraintValidator<Cron, String> {
    @Override
    public boolean isValid(String cron, ConstraintValidatorContext context) {
        return cron != null && CronExpression.isValidExpression(cron);
    }
}