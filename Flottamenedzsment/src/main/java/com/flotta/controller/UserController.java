package com.flotta.controller;

import java.util.Map;
import java.util.Optional;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.model.registry.User;
import com.flotta.service.ServiceManager;

@Controller
public class UserController {

  private ServiceManager service;

  @Autowired
  public void setMainService(ServiceManager service) {
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
  public String prepareCreatingUser(Model model) {
    model.addAttribute("user", new User());
    return "user_templates/userNew";
  }
  
  @PostMapping("/user/new")
  public String createUser(Model model, @ModelAttribute("user") User user) {
    if(service.createUser(user)) {
      return "redirect:/user/all";
    } else {
      model.addAttribute("user", user);
      model.addAttribute("messages", service.getUserError());
      return "user_templates/userNew";
    }
  }
  
  @GetMapping("/user/{id}/update")
  public String prepareUpdatingUser(Model model, @PathVariable("id") long id) {
    Optional<User> userOpt = service.findUserById(id);
    if(userOpt.isPresent()) {
      model.addAttribute("user", userOpt.get());
      return "user_templates/userEdit";
    } else {
      return "redirect:/user/all";
    }
  }
  
  @PostMapping("/user/{id}/update")
  public String updateUser(RedirectAttributes ra, @PathVariable("id") long id, @RequestParam  Map<String, Boolean> roles) {
    if(!service.updateUser(id, roles)) {
      ra.addFlashAttribute("messages", service.getUserError());
    }
    return "redirect/:user/" + id + "/update";
  }
  
  // ----- Guest -----
  
  @GetMapping("/registration")
  public String firstAdminRegistration(Model model, RedirectAttributes redirectAttributes) {
    if(service.registrationAvailable()) {
      model.addAttribute("user", new User());
      return "registration";
    } else {
      redirectAttributes.addFlashAttribute("warning", ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString("warning.alreadyHaveAdministratior"));
      return "redirect:/login";
    }
  }
  
  @PostMapping("/registration")
  public String firstAdminRegistration(Model model, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
    if(service.createFirstAdmin(user)) {
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
    return "newPasswordRequest";
  }
  
  @PostMapping("/requestNewPassword")
  public String requestNewPassword(RedirectAttributes redirectAttributes, @RequestParam("email") String email) {
    if(service.requestNewPassword(email)) {
      redirectAttributes.addFlashAttribute("waring", "The email has been sent to " + email + " address!");
    }
    return "redirect:/login";
  }
  
}