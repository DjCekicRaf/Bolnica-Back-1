package raf.si.bolnica.management.exceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import raf.si.bolnica.management.exceptions.AllergenNotExistException;
import raf.si.bolnica.management.exceptions.ErrorResponse;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MissingRequestFieldsException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestFieldsException(MissingRequestFieldsException e) {
        return new ResponseEntity<>(e.getErrorResponse(), e.getErrorResponse().getResponseStatus());
    }

    @ExceptionHandler(value = AllergenNotExistException.class)
    public ResponseEntity<ErrorResponse> handleAllergenAlreadyExist(AllergenNotExistException e) {
        return new ResponseEntity<>(e.getErrorResponse(), e.getErrorResponse().getResponseStatus());
    }
}
