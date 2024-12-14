package com.dentalapp.backend.configuration;

import com.dentalapp.backend.model.appointment.exceptions.AppointmentNotFoundException;
import com.dentalapp.backend.model.appointment.exceptions.IllegalAppointmentDate;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityAlreadyExistsException;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityNotFoundException;
import com.dentalapp.backend.model.invoice.exceptions.InvoiceNotFoundException;
import com.dentalapp.backend.model.prescription.exceptions.PrescriptionNotFoundException;
import com.dentalapp.backend.model.referral.exception.ReferralNotFoundException;
import com.dentalapp.backend.model.supplies.exceptions.SupplyAlreadyExistsException;
import com.dentalapp.backend.model.supplies.exceptions.SupplyNotFoundException;
import com.dentalapp.backend.model.token.exceptions.InvalidTokenException;
import com.dentalapp.backend.model.token.exceptions.TokenAlreadyUsedException;
import com.dentalapp.backend.model.token.exceptions.TokenExpiredException;
import com.dentalapp.backend.model.treatment.exceptions.TreatmentAlreadyExistsException;
import com.dentalapp.backend.model.treatment.exceptions.TreatmentNotFoundException;
import com.dentalapp.backend.model.user.exceptions.UserAlreadyExistsException;
import com.dentalapp.backend.model.user.exceptions.UserAuthenticationException;
import com.dentalapp.backend.model.user.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        if (!e.getBindingResult().getFieldErrors().isEmpty()) {
            e.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
            String errorMessage = String.join(", ", errorMap.values());
            return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(BAD_REQUEST.value(), errorMessage));
        } else {
            e.getBindingResult().getAllErrors().forEach(error -> errorMap.put(error.getObjectName(), error.getDefaultMessage()));
            String errorMessage = String.join(", ", errorMap.values());
            return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(BAD_REQUEST.value(), errorMessage));
        }
    }

    @ExceptionHandler(value = UserAuthenticationException.class)
    public ResponseEntity<?> handleUserAuthenticationException(UserAuthenticationException e) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(value = AppointmentNotFoundException.class)
    public ResponseEntity<?> handleAppointmentNotFoundException(AppointmentNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = IllegalAppointmentDate.class)
    public ResponseEntity<?> handleIllegalAppointmentDate(IllegalAppointmentDate e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = TokenAlreadyUsedException.class)
    public ResponseEntity<?> handleTokenAlreadyUsedException(TokenAlreadyUsedException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = AvailabilityAlreadyExistsException.class)
    public ResponseEntity<?> handleAvailabilityAlreadyExistsException(AvailabilityAlreadyExistsException e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(value = TreatmentNotFoundException.class)
    public ResponseEntity<?> handleTreatmentNotFoundException(TreatmentNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = TreatmentAlreadyExistsException.class)
    public ResponseEntity<?> handleTreatmentAlreadyExistsException(TreatmentAlreadyExistsException e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(value = SupplyAlreadyExistsException.class)
    public ResponseEntity<?> handleSupplyAlreadyExistsException(SupplyAlreadyExistsException e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(value = SupplyNotFoundException.class)
    public ResponseEntity<?> handleSupplyNotFoundException(SupplyNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = PrescriptionNotFoundException.class)
    public ResponseEntity<?> handlePrescriptionNotFoundException(PrescriptionNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = ReferralNotFoundException.class)
    public ResponseEntity<?> handleReferralNotFoundException(ReferralNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = InvoiceNotFoundException.class)
    public ResponseEntity<?> handleInvoiceNotFoundException(InvoiceNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = AvailabilityNotFoundException.class)
    public ResponseEntity<?> handleAvailabilityNotFoundException(AvailabilityNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }
}
