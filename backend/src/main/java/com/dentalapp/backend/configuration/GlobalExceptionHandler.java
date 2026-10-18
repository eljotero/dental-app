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
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindingException(BindException e) {
        BindingResult result = e.getBindingResult();
        String message = result.getAllErrors().stream().map(error -> {
            if (error instanceof FieldError fieldError) {
                if (fieldError.isBindingFailure()) {
                    return invalidParameterMessage(fieldError.getField(), result.getFieldType(fieldError.getField()));
                }
                return fieldError.getField() + ": " + Objects.requireNonNullElse(fieldError.getDefaultMessage(), "invalid value");
            }
            return Objects.requireNonNullElse(error.getDefaultMessage(), "Invalid request parameters");
        }).distinct().collect(Collectors.joining("; "));

        return badRequest(message.isBlank() ? "Invalid request parameters" : message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleParameterTypeMismatch(MethodArgumentTypeMismatchException e) {
        return badRequest(invalidParameterMessage(e.getName(), e.getRequiredType()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameter(MissingServletRequestParameterException e) {
        return badRequest("Parameter '" + e.getParameterName() + "' is required");
    }

    private ResponseEntity<ErrorResponse> badRequest(String message) {
        return ResponseEntity.badRequest().body(new ErrorResponse(BAD_REQUEST.value(), message));
    }

    private String invalidParameterMessage(String name, Class<?> requiredType) {
        String prefix = "Invalid parameter '" + name + "'. ";

        if (requiredType == null) {
            return prefix + "Check the value and format";
        }
        if (requiredType.isEnum()) {
            String allowedValues = Arrays.stream(requiredType.getEnumConstants()).map(value -> ((Enum<?>) value).name()).collect(Collectors.joining(", "));
            return prefix + "Allowed values: " + allowedValues;
        }
        if (requiredType == LocalDate.class) {
            return prefix + "Expected a date in yyyy-MM-dd format";
        }
        if (requiredType == LocalTime.class) {
            return prefix + "Expected a time in HH:mm or HH:mm:ss format";
        }
        if (requiredType == LocalDateTime.class) {
            return prefix + "Expected a date and time in yyyy-MM-dd'T'HH:mm:ss format";
        }
        if (requiredType == Boolean.class || requiredType == boolean.class) {
            return prefix + "Allowed values: true, false";
        }
        if (requiredType == Byte.class || requiredType == byte.class || requiredType == Short.class || requiredType == short.class || requiredType == Integer.class || requiredType == int.class || requiredType == Long.class || requiredType == long.class || requiredType == java.math.BigInteger.class) {
            return prefix + "Expected a whole number within the supported range";
        }
        if (Number.class.isAssignableFrom(requiredType) || requiredType == float.class || requiredType == double.class) {
            return prefix + "Expected a number within the supported range";
        }
        return prefix + "Check the value and format";
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
