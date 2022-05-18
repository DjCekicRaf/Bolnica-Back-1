package raf.si.bolnica.management.exceptions;

import org.springframework.http.HttpStatus;


public class VaccineNotExistException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public VaccineNotExistException(String message, String developerMessage) {
        super(message);
        errorResponse = new ErrorResponse();

        errorResponse.setDeveloperMsg(developerMessage);
        errorResponse.setErrorMsg(message);
        errorResponse.setResponseCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setResponseStatus(HttpStatus.BAD_REQUEST);
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

}
