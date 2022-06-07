package com.up42.backend.coding.challenge.exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Controller Advice class - Which handles any exceptions and constructs a proper response,
 * to provide user with correct information of the error and avoid exposing internals
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  private final String INCORRECT_REQUEST = "INCORRECT_REQUEST";
  private final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
  private final String FILE_SYSTEM_ERROR = "FILE_SYSTEM_ERROR";

  @ExceptionHandler(FeatureNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleFeatureNotFoundException(FeatureNotFoundException ex, WebRequest request) {
    List<String> details = new ArrayList<>();
    details.add(ex.getLocalizedMessage());
    ErrorResponse error = new ErrorResponse(INCORRECT_REQUEST, details);
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = IOException.class)
  public ResponseEntity<ErrorResponse> handleIORuntimeException(IOException ex, WebRequest request) {
    List<String> details = new ArrayList<>();
    log.error("File system error: "+ ex.getMessage(), ex);
    details.add(ex.getLocalizedMessage());
    ErrorResponse error = new ErrorResponse(FILE_SYSTEM_ERROR, details);
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedRuntimeException(Exception ex, WebRequest request) {
    List<String> details = new ArrayList<>();
    log.error("Unknown error: "+ ex.getMessage(), ex);
    details.add(ex.getLocalizedMessage());
    ErrorResponse error = new ErrorResponse(UNKNOWN_ERROR, details);
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
