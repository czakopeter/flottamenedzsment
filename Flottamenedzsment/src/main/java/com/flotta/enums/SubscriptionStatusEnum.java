package com.flotta.enums;

public enum SubscriptionStatusEnum {
  FREE(0),
  ACTIVE(1),
  RESIGN(2),
  TOOK(3),
  SUSPEND(4);
  
  private int code;
  
  private SubscriptionStatusEnum(int code) {
    this.code = code;
  }
  
  public int getCode( ) {
    return code;
  }
  
  public boolean isFree() {
    return 0 == this.code;
  }
}
