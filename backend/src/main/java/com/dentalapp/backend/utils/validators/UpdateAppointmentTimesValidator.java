package com.dentalapp.backend.utils.validators;

import com.dentalapp.backend.utils.constraints.UpdateAppointmentTimesConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalTime;

public class UpdateAppointmentTimesValidator implements ConstraintValidator<UpdateAppointmentTimesConstraint, Object> {
    private String startTime;
    private String endTime;

    @Override
    public void initialize(UpdateAppointmentTimesConstraint constraintAnnotation) {
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

        if (startTimeValue == null && endTimeValue == null) {
            return true;
        }

        if (startTimeValue == null || endTimeValue == null) {
            return true;
        }

        try {
            LocalTime start = LocalTime.parse(startTimeValue);
            LocalTime end = LocalTime.parse(endTimeValue);

            return start.isBefore(end);
        } catch (Exception e) {
            return false;
        }
    }
}
