package com.info.baymax.common.comp.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SexConstraintValidator implements ConstraintValidator<Sex, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && (value.equals("男") || value.equals("女"));
    }
}