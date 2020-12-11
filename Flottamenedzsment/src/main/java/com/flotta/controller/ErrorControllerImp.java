package com.flotta.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flotta.enums.MessageKey;

@Controller
public class ErrorControllerImp implements ErrorController {
  
  @ModelAttribute
  public void prepareController(Model model) {
    model.addAttribute("locale", LocaleContextHolder.getLocale().getLanguage());
  }

  @RequestMapping("/accessDenied")
  public String accesDenied() {
    return "accessDenied";
  }
  
  //TODO error-ok lekezelése
  @RequestMapping("/error")
  public String error(Model model, HttpServletRequest request) {
//    Enumeration<String> s = request.getAttributeNames();
//    while(s.hasMoreElements()) {
//      System.err.println(s.nextElement());
//    }
    
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    String uri = (String)request.getAttribute("javax.servlet.error.request_uri");
    
    
    
    if(statusCode.equals(404)) {
      model.addAttribute("message", MessageKey.PAGE_NOT_FOUND);
    } else {
      model.addAttribute("message", MessageKey.UNKNOWN_FAILURE);
    }
    model.addAttribute("uri", uri);
    model.addAttribute("statusCode", statusCode);
    
      return "error";
  }

  @Override
  public String getErrorPath() {
    // TODO Auto-generated method stub
    return null;
  }
  
}
