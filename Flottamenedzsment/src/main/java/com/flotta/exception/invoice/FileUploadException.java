package com.flotta.exception.invoice;


import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;

public class FileUploadException extends Exception {

  private static final long serialVersionUID = -1413227318186800424L;
  
  MessageKey key;
  MessageType type;

  public FileUploadException(String msg) {
    super(msg);
  }
  
  public FileUploadException(MessageKey key, MessageType type) {
    this.key = key;
    this.type = type;
  }

  public MessageKey getKey() {
    return key;
  }

  public MessageType getType() {
    return type;
  }
} 
