package com.flotta.enums;

public enum Availability {
  AVAILABLE(0, "available"),
  NONE(1, "noneAvailable");
  
  private int code;
  
  private String key;

  private Availability(int code, String key) {
    this.code = code;
    this.key = key;
  }
  
  public int getCode() {
    return code;
  }
  
  public String getKey() {
    return key;
  }
  
}
