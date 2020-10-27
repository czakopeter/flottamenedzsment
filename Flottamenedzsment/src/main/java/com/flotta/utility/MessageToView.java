package com.flotta.utility;

public class MessageToView {
  
  private String[] types = {"alert-success", "alert-warning", "alert-danger"};
  
  private String type;
  
  private String text;
  
  public MessageToView(String text) {
    this.text = text;
  }
  
  public void setToSuccess() {
    type = types[0];
  }
  
  public void setToWarning() {
    type = types[1];
  }
  
  public void setToDanger() {
    type = types[2];
  }
}
