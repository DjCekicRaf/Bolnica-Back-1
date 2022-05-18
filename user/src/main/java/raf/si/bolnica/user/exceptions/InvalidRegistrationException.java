package raf.si.bolnica.user.exceptions;

import org.springframework.http.HttpStatus;


public class InvalidRegistrationException extends RuntimeException {
    private ErrorResponse errorResponse;

    public InvalidRegistrationException(String message, String developerMessage) {
        super(message);
        errorResponse = new ErrorResponse();

        errorResponse.setDeveloperMsg(developerMessage);
        errorResponse.setErrorMsg(message);
        errorResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
