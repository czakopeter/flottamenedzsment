package com.flotta.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

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