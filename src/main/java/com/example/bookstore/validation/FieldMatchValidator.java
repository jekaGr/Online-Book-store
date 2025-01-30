package com.example.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field firstField = value.getClass().getDeclaredField(firstFieldName);
            firstField.setAccessible(true);
            Object firstObj = firstField.get(value);

            Field secondField = value.getClass().getDeclaredField(secondFieldName);
            secondField.setAccessible(true);
            Object secondObj = secondField.get(value);

            return firstObj != null && firstObj.equals(secondObj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
