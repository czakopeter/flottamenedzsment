package com.flotta.enums;

public enum UserStatus {
  WAITING_FOR_ACTIVATION(0),
  ENABLED(1),
  DISABLED(2);
  
  private int code;
  
  private UserStatus(int code) {
    this.code = code;
  }
  
  public int getCode( ) {
    return code;
  }
  
  public boolean equals(UserStatus status) {
    if(this.compareTo(status) == 0) {
      return true;
    }
    return false;
  }
}
