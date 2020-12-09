package com.flotta.controller;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.enums.ControllerType;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.registry.User;
import com.flotta.service.MessageService;
import com.flotta.service.ServiceManager;
import com.flotta.utility.ExtendedBoolean;
import com.flotta.utility.ResponseTransfer;

@Controller
public class UserController {

  @Autowired
  private ServiceManager service;

  @Autowired
  private MessageService messageService;
  
  @ModelAttribute
  public void prepareController(Model model) {
    model.addAttribute("title", "User");
    model.addAttribute("locale", LocaleContextHolder.getLocale().getCountry());
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
    messageService.addMessage(eb);
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
      messageService.addMessage(MessageKey.UNKNOWN_USER, MessageType.ERROR);
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
      messageService.addMessage(eb);
    }
    return "redirect:/user/" + id + "/update";
  }
  
  // ----- Guest -----
  
  @GetMapping("/login")
  public String login(Model model) {
    boolean hasAdmin = service.hasAdmin();
    boolean hasEnabledAdmin = service.hasEnabledAdmin();
    model.addAttribute("messages", messageService.getMessages());
    if(hasEnabledAdmin) {
      model.addAttribute("hasRegBtn", false);
      return "auth/login";
    } else if(hasAdmin){
      model.addAttribute("hasRegBtn", true);
      return "auth/login";
    } else {
      return "redirect:/registration";
    }
        
  }
  
  @RequestMapping("/login/failure")
  public String loginFailure() {
    messageService.addMessage(MessageKey.LOGIN_FAILURE, MessageType.WARNING);
    return "redirect:/login";
  }
  
  @RequestMapping("/afterlogout")
  public String logout() {
    messageService.addMessage(MessageKey.SUCCESSFUL_LOGOUT, MessageType.SUCCESS);
    return "redirect:/login";
  }
  
  @GetMapping("/registration")
  public String firstAdminRegistration(Model model, RedirectAttributes redirectAttributes) {
    boolean hasEnabledAdmin = service.hasEnabledAdmin();
    if(hasEnabledAdmin) {
      messageService.addMessage(MessageKey.ALREADY_HAS_ADMIN , MessageType.WARNING);
      return "redirect:/login";
    } else {
      boolean hasAdmin = service.hasAdmin();
      if(hasAdmin) {
        messageService.addMessage(MessageKey.ACTIVATE_OR_CREATE_FIRST_ADMIN, MessageType.WARNING);
      } else {
        messageService.addMessage(MessageKey.CREATE_FIRST_ADMIN, MessageType.WARNING);
      }
      model.addAttribute("messages", messageService.getMessages());
      model.addAttribute("user", new User());
      return "registration";
    } 
  }
  
  @PostMapping("/registration")
  public String firstAdminRegistration(Model model, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
    ExtendedBoolean eb = service.createFirstAdmin(user);
    messageService.addMessage(eb);
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
    messageService.addMessage(eb);
    return "redirect:/login";
  }
  
  @GetMapping("/requestNewPassword")
  public String prepareRequestingNewPassword(Model model) {
    model.addAttribute("messages", messageService.getMessages());
    return "newPasswordRequest";
  }
  
  @PostMapping("/requestNewPassword")
  public String requestNewPassword(@RequestParam("email") String email) {
    ExtendedBoolean eb = service.requestNewPassword(email);
    messageService.addMessage(eb);
    return "redirect:/requestNewPassword";
  }
  
  @DeleteMapping("user/delete")
  @ResponseBody
  public ResponseTransfer deleteUser(@RequestParam("email") String email) {
    service.deleteUserById(email);
    return new ResponseTransfer(email);
  }
  
}