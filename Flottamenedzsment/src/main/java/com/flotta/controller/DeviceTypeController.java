package com.flotta.controller;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flotta.model.registry.DeviceType;
import com.flotta.service.ServiceManager;

@Controller
public class DeviceTypeController {

  private ServiceManager service;

  @Autowired
  public void setMainService(ServiceManager service) {
    this.service = service;
  }
  
  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "DeviceType");
  }

  @RequestMapping("/deviceType/all")
  public String listDeviceTypes(Model model) {
    model.addAttribute("deviceTypes", service.findAllDeviceTypes());
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
    if(service.createDeviceType(deviceType)) {
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
      return "redirect:/diviceType/all";
    }
  }
  
  @PostMapping("/deviceType/{id}/update")
  public String updateDeviceType(Model model, @ModelAttribute DeviceType deviceType) {
    service.updateDeviceType(deviceType);
    return "redirect:/deviceType/" + deviceType.getId() + "/update";
  }
}