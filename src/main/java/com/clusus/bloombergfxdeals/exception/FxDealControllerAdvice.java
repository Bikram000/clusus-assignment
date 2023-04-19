package com.clusus.bloombergfxdeals.exception;

import com.clusus.bloombergfxdeals.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class FxDealControllerAdvice {

  /**
   * handles any {@link MethodArgumentNotValidException} if customer sends invalid request
   *
   * @param {@link MethodArgumentNotValidException}
   * @return {@link ResponseEntity} of type {@link Map}
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    for (FieldError fieldError : fieldErrors) {
      String errorField = fieldError.getField();
      String errorMessage = fieldError.getDefaultMessage();
      errors.put(errorField, errorMessage);
    }

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * handles any {@link BusinessException}
   *
   * @param {@link BusinessException}
   * @return {@link ResponseEntity} of type {@link ErrorResponse}
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {

    return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * handles any {@link IllegalArgumentException}
   *
   * @param {@link BusinessException}
   * @return {@link ResponseEntity} of type {@link String}
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {

    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
