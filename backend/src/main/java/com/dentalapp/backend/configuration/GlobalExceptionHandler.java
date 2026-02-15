package com.dentalapp.backend.configuration;

import com.dentalapp.backend.model.appointment.exceptions.AppointmentNotFoundException;
import com.dentalapp.backend.model.appointment.exceptions.IllegalAppointmentDate;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityAlreadyExistsException;
import com.dentalapp.backend.model.availability.exceptions.AvailabilityNotFoundException;
import com.dentalapp.backend.model.enums.exceptions.PaymentMethodNotFoundException;
import com.dentalapp.backend.model.enums.exceptions.PaymentStatusNotFoundException;
import com.dentalapp.backend.model.file.exceptions.FileIsEmptyException;
import com.dentalapp.backend.model.file.exceptions.FileNameAlreadyExists;
import com.dentalapp.backend.model.file.exceptions.FileNotFoundException;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
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
    public ResponseEntity<ErrorResponse> handleUserAuthenticationException(UserAuthenticationException e) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(value = AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotFoundException(AppointmentNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = IllegalAppointmentDate.class)
    public ResponseEntity<ErrorResponse> handleIllegalAppointmentDate(IllegalAppointmentDate e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = TokenAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleTokenAlreadyUsedException(TokenAlreadyUsedException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(TokenExpiredException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(value = AvailabilityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAvailabilityAlreadyExistsException(AvailabilityAlreadyExistsException e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(value = TreatmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTreatmentNotFoundException(TreatmentNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = TreatmentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTreatmentAlreadyExistsException(TreatmentAlreadyExistsException e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(value = SupplyAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleSupplyAlreadyExistsException(SupplyAlreadyExistsException e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(value = SupplyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSupplyNotFoundException(SupplyNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = PrescriptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePrescriptionNotFoundException(PrescriptionNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = ReferralNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReferralNotFoundException(ReferralNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = InvoiceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInvoiceNotFoundException(InvoiceNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = AvailabilityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAvailabilityNotFoundException(AvailabilityNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = PaymentStatusNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentStatusNotFoundException(PaymentStatusNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = PaymentMethodNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentMethodNotFoundException(PaymentMethodNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(value = FileNameAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleFileNameAlreadyExists(FileNameAlreadyExists e) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException() {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "File size exceeds the limit"));
    }

    @ExceptionHandler(value = FileIsEmptyException.class)
    public ResponseEntity<ErrorResponse> handleFileIsEmptyException(FileIsEmptyException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
}
