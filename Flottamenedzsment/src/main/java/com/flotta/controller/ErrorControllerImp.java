package com.flotta.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.flotta.enums.MessageKey;

@Controller
public class ErrorControllerImp implements ErrorController {

  @RequestMapping("/accessDenied")
  public String accesDenied() {
    
    System.err.println("accessDenied");
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
    
//      WebRequest wr = new ServletWebRequest(request);
//      Map<String, Object> errors = this.errorAttributes.getErrorAttributes(wr, ErrorAttributeOptions.of(Include.STACK_TRACE));
//      model.addAttribute("errors", errors);
      
      return "error";
  }
  
  @Deprecated
  @Override
  public String getErrorPath() {
    return null;
  }
  
  
//  @ResponseStatus(value = HttpStatus.NOT_FOUND)
//  public String pageNotFound (Model model) {
//    model.addAttribute("message", "NOT_FOUND");
//    return "accessDenied";
//  }
//  
//  
//  @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
//  public String pageMethodNotAllowed (Model model) {
//    model.addAttribute("message", "METHOD_NOT_ALLOWED");
//    return "accessDenied";
//  }
}
