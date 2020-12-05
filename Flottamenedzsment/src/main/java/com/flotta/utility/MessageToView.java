package com.flotta.utility;

import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;

public class MessageToView {
  
  private String key;
  
  private String css;
  
  public MessageToView(String key, String type) {
    this.key = key;
    this.css = type;
  }
  
  public MessageToView(MessageKey key, MessageType value) {
    this.key = key.getKey();
    this.css = value.getType();
  }

  public String getCss() {
    return css;
  }

  public String getKey() {
    return key;
  }
}