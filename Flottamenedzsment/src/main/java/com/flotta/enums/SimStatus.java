package com.flotta.enums;

public enum SimStatus {
  FREE(0),
  ACTIVE(1),
  CHANGED(2);
  
  private int code;
  
  private SimStatus(int code) {
    this.code = code;
  }
  
  public int getCode( ) {
    return code;
  }
  
  public boolean isFree() {
    return 0 == this.code;
  }
}
