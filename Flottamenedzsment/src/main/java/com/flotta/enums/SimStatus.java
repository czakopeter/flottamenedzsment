package com.flotta.enums;

public enum SimStatus {
  FREE(0, "free"),
  USED(1, "used"),
  CHANGED(2, "changed");
  
  private int code;
  
  private String key;
  
  private SimStatus(int code, String key) {
    this.code = code;
    this.key = key;
  }
  
  public int getCode( ) {
    return code;
  }
  
  public boolean isFree() {
    return 0 == this.code;
  }
  
  public String getKey() {
    return key;
  }
}
