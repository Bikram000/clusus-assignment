package com.clusus.bloombergfxdeals.exception;

import com.clusus.bloombergfxdeals.dto.ErrorResponse;

public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final ErrorResponse response;

  public BusinessException(ErrorResponse response) {
    super(response.getErrorDescription());
    this.response = response;
  }

  public ErrorResponse getErrorResponse() {
    return response;
  }
}
