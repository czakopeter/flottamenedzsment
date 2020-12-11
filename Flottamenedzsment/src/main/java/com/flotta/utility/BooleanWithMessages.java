package com.flotta.utility;

import java.util.LinkedList;
import java.util.List;

import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;

public class BooleanWithMessages {
  
  private boolean value;
  
  private List<MessageToView> messages = new LinkedList<>();
  
  public BooleanWithMessages(boolean value) {
    this.value = value;
  }
  
  public boolean booleanValue() {
    return value;
  }

  public void setTrue() {
    this.value = true;
  }
  
  public void setFalse() {
    this.value = false;
  }
  
  public List<MessageToView> getMessages() {
    return messages;
  }

  public void addMessage(MessageKey key, MessageType type) {
    messages.add(new MessageToView(key, type));
  }
}
