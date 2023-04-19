package com.clusus.bloombergfxdeals.dto;

public class FinalResponse {
  private String message;

  public FinalResponse() {}

  public FinalResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
