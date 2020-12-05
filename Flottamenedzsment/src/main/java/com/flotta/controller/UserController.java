package com.flotta.controller;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.enums.ControllerType;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.registry.User;
import com.flotta.service.MessageService;
import com.flotta.service.ServiceManager;
import com.flotta.utility.ExtendedBoolean;

@Controller
public class UserController {

  @Autowired
  private ServiceManager service;

  @Autowired
  private MessageService messageService;
  
  @ModelAttribute
  public void prepareController(Model model) {
    model.addAttribute("title", "User");
    messageService.setActualController(ControllerType.USER);
  }
  
  @GetMapping("/user/all")
  public String listUsers(Model model) {
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("messages", messageService.getMessages());
    return "user_templates/userAll";
  }

  @GetMapping("/user/new")
  public String prepareCreatingUser(Model model) {
    model.addAttribute("user", new User());
    return "user_templates/userNew";
  }
  
  @PostMapping("/user/new")
  public String createUser(Model model, @ModelAttribute("user") User user) {
    ExtendedBoolean eb = service.createUser(user);
    messageService.clearAndAddMessage(eb);
    if(eb.isValid()) {
      return "redirect:/user/all";
    } else {
      model.addAttribute("user", user);
      model.addAttribute("messages", messageService.getMessages());
      return "user_templates/userNew";
    }
  }
  
  @GetMapping("/user/{id}/update")
  public String prepareUpdatingUser(Model model, @PathVariable("id") long id) {
    Optional<User> userOpt = service.findUserById(id);
    if(userOpt.isPresent()) {
      model.addAttribute("user", userOpt.get());
      model.addAttribute("messages", messageService.getMessages());
      return "user_templates/userEdit";
    } else {
      messageService.clearAndAddMessage(MessageKey.NOT_EXISTS, MessageType.WARNING);
      return "redirect:/user/all";
    }
  }
  
  @PostMapping("/user/{id}/update")
  public String updateUser(RedirectAttributes ra, @PathVariable("id") long id, @RequestParam(required = false) Map<String, Boolean> roles) {
    if(roles == null) {
      roles = Collections.emptyMap();
    }
    ExtendedBoolean eb = service.updateUser(id, roles);
    if(!eb.isValid()) {
      messageService.clearAndAddMessage(eb);
    }
    return "redirect:/user/" + id + "/update";
  }
  
  // ----- Guest -----
  
  @GetMapping("/registration")
  public String firstAdminRegistration(Model model, RedirectAttributes redirectAttributes) {
    ExtendedBoolean eb = service.registrationAvailable();
    if(eb.isValid()) {
      model.addAttribute("user", new User());
      return "registration";
    } else {
      messageService.clearAndAddMessage(eb);
      return "redirect:/login";
    }
  }
  
  @PostMapping("/registration")
  public String firstAdminRegistration(Model model, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
    ExtendedBoolean eb = service.createFirstAdmin(user);
    messageService.clearAndAddMessage(eb);
    if(eb.isValid()) {
      return "redirect:/login";
    } else {
      model.addAttribute("user", user);
      return "registration";
    }
  }
  
  @GetMapping("/activation/{key}")
  public String activation(@PathVariable("key") String key, RedirectAttributes redirectAttributes) {
    ExtendedBoolean eb = service.activation(key);
    messageService.clearAndAddMessage(eb);
    return "redirect:/login";
  }
  
  @GetMapping("/requestNewPassword")
  public String prepareRequestingNewPassword() {
    return "newPasswordRequest";
  }
  
  @PostMapping("/requestNewPassword")
  public String requestNewPassword(RedirectAttributes redirectAttributes, @RequestParam("email") String email) {
    ExtendedBoolean eb = service.requestNewPassword(email);
    messageService.clearAndAddMessage(eb);
    return "newPasswordRequest";
  }
  
}