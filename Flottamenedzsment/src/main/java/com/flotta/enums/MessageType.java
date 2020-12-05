package com.flotta.enums;

public enum MessageType {
  SUCCESS("alert-success"),
  WARNING("alert-warning"),
  ERROR("alert-danger");
  
  private String type;
  
  MessageType(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }
}
