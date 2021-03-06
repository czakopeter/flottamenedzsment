package com.flotta.enums;

public enum SimStatusEnum {
  FREE(0),
  ACTIVE(1),
  CHANGED(2),
  STOLE(3),
  LOST(4);
  
  private int code;
  
  private SimStatusEnum(int code) {
    this.code = code;
  }
  
  public int getCode( ) {
    return code;
  }
  
  public boolean isFree() {
    return 0 == this.code;
  }
}
