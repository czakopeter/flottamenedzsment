package com.flotta.enums;

public enum UserStatus {
  WAITING_FOR_ACTIVATION(0, "notActivated"),
  ENABLED(1, "enabled"),
  DISABLED(2, "disabled");
  
  private int code;
  
  private String key;
  
  private UserStatus(int code, String key) {
    this.code = code;
    this.key = key;
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
  
  public String getKey() {
    return key;
  }
}
