package com.flotta.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flotta.service.MainService;

@Controller
public class HomeController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "Homepage");
  }
  
  @RequestMapping("/accessDenied")
  public String accessDenied(Model model) {
    model.addAttribute("title", "no title");
    model.addAttribute("error", "Access denied");
    return "accessDenied";
  }
  
  @RequestMapping("/loginError")
  public String loginError(Model model) {
    model.addAttribute("error", "Incorrect email or password!");
    return "auth/login";
  }
  
  @GetMapping("/login")
  public String login() {
    return "auth/login";
  }
  
}