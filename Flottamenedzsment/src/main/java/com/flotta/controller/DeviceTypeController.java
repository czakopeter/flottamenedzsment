package com.flotta.controller;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flotta.enums.ControllerType;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.registry.DeviceType;
import com.flotta.service.MessageService;
import com.flotta.service.ServiceManager;
import com.flotta.utility.ExtendedBoolean;
import com.flotta.utility.Utility;

@Controller
public class DeviceTypeController {

  private ServiceManager service;
  
  @Autowired
  private MessageService messageService;

  @Autowired
  public void setMainService(ServiceManager service) {
    this.service = service;
  }
  
  @ModelAttribute
  public void prepareController(Model model) {
    model.addAttribute("title", "DeviceType");
    model.addAttribute("locale", LocaleContextHolder.getLocale().getCountry());
    messageService.setActualController(ControllerType.DEVICE_TYPE);
  }

  @RequestMapping("/deviceType/all")
  public String listDeviceTypes(Model model) {
    model.addAttribute("deviceTypes", Utility.sortDeviceTypeByName(service.findAllDeviceTypes()));
    model.addAttribute("messages", messageService.getMessages());
    return "device_type_templates/deviceTypeAll";
  }
  
  @GetMapping("/deviceType/new")
  public String prepareCreatingDeviceTypes(Model model) {
    model.addAttribute("brandList", service.findAllBrandOfDevicesType());
    model.addAttribute("deviceType", new DeviceType());
    return "device_type_templates/deviceTypeNew";
  }
  
  @PostMapping("/deviceType/new")
  public String createDeviceTypes(Model model, @ModelAttribute("deviceType") DeviceType deviceType) {
    ExtendedBoolean eb = service.createDeviceType(deviceType);
    messageService.addMessage(eb);
    if(eb.isValid()) {
      return "redirect:/deviceType/all";
    } else {
      model.addAttribute("brandList", service.findAllBrandOfDevicesType());
      model.addAttribute("deviceType", deviceType);
    }
    return "device_type_templates/deviceTypeNew";
  }
  
  @GetMapping("/deviceType/{id}/update")
  public String prepareUpdatingDeviceType(Model model, @PathVariable("id") long id) {
    Optional<DeviceType> deviceTypeOpt = service.findDeviceTypeById(id);
    if(deviceTypeOpt.isPresent()) {
      model.addAttribute("deviceType", deviceTypeOpt.get());
      return "device_type_templates/deviceTypeEdit";
    } else {
      messageService.addMessage(MessageKey.UNKNOWN_DEVICE_TYPE, MessageType.ERROR);
      return "redirect:/diviceType/all";
    }
  }
  
  @PostMapping("/deviceType/{id}/update")
  public String updateDeviceType(Model model, @ModelAttribute DeviceType deviceType) {
    service.updateDeviceType(deviceType);
    return "redirect:/deviceType/" + deviceType.getId() + "/update";
  }
}