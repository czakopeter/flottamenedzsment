package com.flotta.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerImp implements ErrorController {

  //TODO error-ok lekezelése
  @RequestMapping("/error")
  public String error(Model model, HttpServletRequest request) {
      
    model.addAttribute("message", request.getContextPath());
    
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
