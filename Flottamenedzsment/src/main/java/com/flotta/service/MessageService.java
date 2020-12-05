package com.flotta.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.flotta.enums.ControllerType;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.utility.ExtendedBoolean;
import com.flotta.utility.MessageToView;

@Component
public class MessageService {
  
  private List<MessageToView> messageList = new LinkedList<>();
  
  private ControllerType controllerType;
  
  private boolean firstGet = true;
  
  private Locale locale;
  
  public MessageService() {
    this.controllerType = ControllerType.NONE;
    firstGet = true;
    locale = LocaleContextHolder.getLocale();
  }

  public List<MessageToView> getMessages() {
    if(messageList.isEmpty()) {
      locale = LocaleContextHolder.getLocale();
      return new LinkedList<>();
    }
    if(!LocaleContextHolder.getLocale().equals(locale)) {
      locale = LocaleContextHolder.getLocale();
      firstGet = false;
      return messageList;
    }
    if(firstGet) {
      firstGet = false;
      return messageList;
    }
    messageList.clear();
    return messageList;
  }
  
  public void addMessage(MessageKey key, MessageType type) {
    messageList.add(new MessageToView(key.getKey(), type.getType()));
    firstGet = true;
  }
  
  public void clearAndAddMessage(MessageKey key, MessageType type) {
    messageList.clear();
    messageList.add(new MessageToView(key.getKey(), type.getType()));
    firstGet = true;
  }
  
  public void addMessage(ExtendedBoolean eb) {
    for(Entry<MessageKey, MessageType> entry : eb.getMessages().entrySet()) {
      messageList.add(new MessageToView(entry.getKey(), entry.getValue()));
    }
    firstGet = true;
  }
  
  public void clearAndAddMessage(ExtendedBoolean eb) {
    messageList.clear();
    for(Entry<MessageKey, MessageType> entry : eb.getMessages().entrySet()) {
      messageList.add(new MessageToView(entry.getKey(), entry.getValue()));
    }
    firstGet = true;
  }
  
  public void setActualController(ControllerType currentControllerType) {
    if(!currentControllerType.equals(controllerType)) {
      messageList.clear();
      controllerType = currentControllerType;
    }
  }
}