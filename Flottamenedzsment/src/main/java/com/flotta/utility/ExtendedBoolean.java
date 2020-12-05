package com.flotta.utility;

import java.util.HashMap;
import java.util.Map;

import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;

public class ExtendedBoolean {
  
  private boolean valid;
  
  private Map<MessageKey, MessageType> messages = new HashMap<>();
  
  public ExtendedBoolean(boolean valid) {
    this.valid = valid;
  }
  
  public boolean isValid() {
    return valid;
  }

  public void setValid() {
    this.valid = true;
  }
  
  public void setInvalid() {
    this.valid = false;
  }
  
  public Map<MessageKey, MessageType> getMessages() {
    return messages;
  }

  public void addMessage(MessageKey key, MessageType type) {
    messages.put(key, type);
  }
}
