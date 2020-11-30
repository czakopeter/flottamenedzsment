package com.flotta.utility;

public class ResponseTransfer {

  private String text;

  public ResponseTransfer(String text) {
    this.text = text;
  }
  
  public ResponseTransfer(long value) {
    this.text = Long.toString(value);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
  
  
}
