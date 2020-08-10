package com.flotta.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class ServiceWithMsg {
  Map<String, String> msg = new HashMap<String, String>();
  
  protected void appendMsg(String m) {
    if(m == null || m.isEmpty()) {
      return;
    }
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String old = msg.get(auth.getName());
    if(old == null || old.isEmpty()) {
      msg.put(auth.getName(), m);
    } else {
      msg.put(auth.getName(), old += "/n" + m);
    }
  }
  
  protected String removeMsg() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String result = msg.remove(auth.getName());
    return  result == null ? "" : result ;
  }
  
  protected boolean hasMsg() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return msg.get(auth.getName()) != null;
  }
}