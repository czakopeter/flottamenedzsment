package com.flotta.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flotta.entity.viewEntity.DeviceToView;
import com.flotta.service.MainService;

@Controller
public class DeviceController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "Device");
  }

  @RequestMapping("/device/all")
  public String listDevices(Model model) {
    model.addAttribute("canCreateNew", !service.findAllBrandOfDevicesType().isEmpty());
    model.addAttribute("devices", service.findAllDevices());
    return "device_templates/deviceAll";
  }
  
  @GetMapping("/device/new")
  public String prepareAddingDevice(Model model) {
    model.addAttribute("device", new DeviceToView());
    model.addAttribute("deviceTypes", service.findAllVisibleDeviceTypes());
    return "device_templates/deviceNew";
  }
  
  @PostMapping("/device/new")
  public String addDevice(Model model, @ModelAttribute("device") DeviceToView dtv) {
    if(service.saveDevice(dtv)) {
      return "redirect:/device/all";
    } else {
      model.addAttribute("device", dtv);
      model.addAttribute("deviceTypes", service.findAllDeviceTypes());
      model.addAttribute("error", service.getDeviceServiceError());
      return "device_templates/deviceNew";
    }
  }
  
  @GetMapping("/device/{id}/update")
  public String prepareUpdatingDevice(Model model, @PathVariable("id") long id) {
    model.addAttribute("device", service.findDeviceById(id));
    model.addAttribute("users", service.findAllUser());
    return "device_templates/deviceEdit";
  }
  
  @PostMapping("/device/{id}/update")
  public String updateDevice(Model model,  @ModelAttribute() DeviceToView dtv) {
    if(service.updateDevice(dtv)) {
      model.addAttribute("error", service.getDeviceServiceError());
    }
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("device", service.findDeviceById(dtv.getId()));
    return "device_templates/deviceEdit";
  }
  
  @GetMapping("/device/{id}/view")
  public String viewDevice(Model model, @PathVariable("id") long id) {
    model.addAttribute("device", service.findDeviceById(id));
    model.addAttribute("dates", service.findDeviceDatesById(id));
    return "device_templates/deviceView";
  }
  
  @PostMapping("/device/{id}/view")
  @ResponseBody
  public DeviceToView viewChangeDate(@PathVariable("id") long id, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate date) {
    return service.findDeviceByIdAndDate(id, date);
  }
}
