package com.dentalapp.backend.utils.constraints;

import com.dentalapp.backend.utils.validators.UpdateAppointmentTimesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UpdateAppointmentTimesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateAppointmentTimesConstraint {
    String message() default "Invalid appointment times";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startTime();

    String endTime();
}
