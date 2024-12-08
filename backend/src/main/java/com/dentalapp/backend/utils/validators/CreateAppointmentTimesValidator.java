package com.dentalapp.backend.utils.validators;

import com.dentalapp.backend.utils.constraints.CreateTimesConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalTime;
import java.util.regex.Pattern;

public class CreateAppointmentTimesValidator implements ConstraintValidator<CreateTimesConstraint, Object> {

    private String startTime;
    private String endTime;

    @Override
    public void initialize(CreateTimesConstraint constraintAnnotation) {
        this.startTime = constraintAnnotation.startTime();
        this.endTime = constraintAnnotation.endTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        String startTimeValue = (String) beanWrapper.getPropertyValue(startTime);
        String endTimeValue = (String) beanWrapper.getPropertyValue(endTime);

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        if (startTimeValue == null && endTimeValue == null) {
            return true;
        }

        if (startTimeValue == null || endTimeValue == null) {
            context.buildConstraintViolationWithTemplate("Both start time and end time must be provided")
                    .addConstraintViolation();
            return false;
        }

        String timePattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern pattern = Pattern.compile(timePattern);

        if (!pattern.matcher(startTimeValue).matches()) {
            context.buildConstraintViolationWithTemplate("Invalid start time format")
                    .addPropertyNode(startTime)
                    .addConstraintViolation();
            isValid = false;
        }

        if (!pattern.matcher(endTimeValue).matches()) {
            context.buildConstraintViolationWithTemplate("Invalid end time format")
                    .addPropertyNode(endTime)
                    .addConstraintViolation();
            isValid = false;
        }

        if (!isValid) {
            return false;
        }

        try {
            LocalTime start = LocalTime.parse(startTimeValue);
            LocalTime end = LocalTime.parse(endTimeValue);

            if (!start.isBefore(end)) {
                context.buildConstraintViolationWithTemplate("Start time must be before end time")
                        .addConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            context.buildConstraintViolationWithTemplate("Invalid time format")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}