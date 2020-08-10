package com.flotta.validator;

public class ExtendedBoolean {
  private boolean valid;
  
  private String msg;

  public ExtendedBoolean(boolean valid) {
    this.valid = valid;
  }
  
  public ExtendedBoolean(boolean valid, String msg) {
    this.valid = valid;
    this.msg = msg;
  }
  
  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  
}
