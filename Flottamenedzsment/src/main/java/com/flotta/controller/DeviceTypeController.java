package com.flotta.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flotta.model.registry.DeviceType;
import com.flotta.service.MainService;

@Controller
public class DeviceTypeController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
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
  public String prepareAddingDeviceTypes(Model model) {
    model.addAttribute("brandList", service.findAllBrandOfDevicesType());
    model.addAttribute("deviceType", new DeviceType());
    return "device_type_templates/deviceTypeNew";
  }
  
  @PostMapping("/deviceType/new")
  public String addDeviceTypes(Model model, @ModelAttribute("deviceType") DeviceType deviceType) {
    if(service.saveDeviceType(deviceType)) {
      return "redirect:/deviceType/all";
    } else {
      model.addAttribute("brandList", service.findAllBrandOfDevicesType());
      model.addAttribute("deviceType", deviceType);
    }
    return "device_type_templates/deviceTypeNew";
  }
  
  @GetMapping("/deviceType/{id}/update")
  public String prepareEditingDeviceType(Model model, @PathVariable("id") long id) {
    model.addAttribute("deviceType", service.findDeviceTypeById(id));
    return "device_type_templates/deviceTypeEdit";
  }
  
  @PostMapping("/deviceType/{id}/update")
  public String addDeviceType(Model model, @ModelAttribute DeviceType deviceType) {
    service.updateDeviceType(deviceType);
    return "device_type_templates/deviceTypeEdit";
  }
}