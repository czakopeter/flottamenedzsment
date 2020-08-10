package com.flotta.controller;

import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.entity.User;
import com.flotta.service.MainService;

@Controller
public class UserController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "User");
  }
  
  @GetMapping("/user/all")
  public String listUsers(Model model) {
    model.addAttribute("users", service.findAllUser());
    return "user_templates/userAll";
  }

  @GetMapping("/user/new")
  public String prepareAddingUser(Model model) {
    model.addAttribute("user", new User());
    return "user_templates/userNew";
  }
  
  @PostMapping("/user/new")
  public String addUser(Model model, @ModelAttribute("user") User user) {
    if(service.registerUser(user)) {
      return "redirect:/user/all";
    }
    model.addAttribute("user", user);
    model.addAttribute("error", service.getUserError());
    return "user_templates/userNew";
  }
  
  @GetMapping("/user/{id}")
  public String user(Model model, @PathVariable("id") long id) {
    model.addAttribute("user", service.findUserById(id));
    return "user_templates/userEdit";
  }
  
  @PostMapping("/user/{id}")
  @ResponseBody
  public String editUser(Model model, @PathVariable("id") long id, @RequestParam  Map<String, Boolean> roles) {
    if(!service.updateUser(id, roles)) {
      model.addAttribute("error", service.getUserError());
    }
    model.addAttribute("user", service.findUserById(id));
    return "user_templates/userEdit";
  }
  
  @GetMapping("/registration")
  public String firstUserRegistration(Model model, RedirectAttributes redirectAttributes) {
    if(service.registrationAvailable()) {
      model.addAttribute("user", new User());
      return "registration";
    } else {
      redirectAttributes.addFlashAttribute("warning", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("warning.alreadyHaveAdministratior"));
      return "redirect:/login";
    }
  }
  
  @PostMapping("/registration")
  public String firstUserRegistration(Model model, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
    if(service.firstUserRegistration(user)) {
      redirectAttributes.addFlashAttribute("success", "Successful registration! Activation link and initial password have been sent to "
          + user.getEmail() + 
          " address!");
      return "redirect:/login";
    } else {
      model.addAttribute("user", user);
      model.addAttribute("error", "Invalid full name or email!");
      return "registration";
    }
  }
  
  @GetMapping("/activation/{key}")
  public String activation(@PathVariable("key") String key, RedirectAttributes redirectAttributes) {
    if(service.activation(key)) {
      redirectAttributes.addFlashAttribute("success", "Successful activation!");
    } else {
      redirectAttributes.addFlashAttribute("error", "Invalid key '" + key + "'!");
    }
    return "redirect:/login";
  }
  
  @GetMapping("/requestNewPassword")
  public String prepareRequestingNewPassword() {
    return "/passwordReset";
  }
  
  @PostMapping("/requestNewPassword")
  public String passwordReset(RedirectAttributes redirectAttributes, @RequestParam("email") String email) {
    if(service.passwordReset(email)) {
      redirectAttributes.addFlashAttribute("waring", "The email has been sent to " + email + " address!");
    }
    return "redirect:/login";
  }
  
}