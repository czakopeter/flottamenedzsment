package com.flotta.enums;

public enum UserStatusEnum {
  WAITING_FOR_ACTIVATION(0),
  ENABLED(1),
  DISABLED(2);
  
  private int code;
  
  private UserStatusEnum(int code) {
    this.code = code;
  }
  
  public int getCode( ) {
    return code;
  }
  
  public boolean equals(UserStatusEnum status) {
    if(this.compareTo(status) == 0) {
      return true;
    }
    return false;
  }
}
