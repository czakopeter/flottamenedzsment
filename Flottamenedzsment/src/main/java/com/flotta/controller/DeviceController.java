package com.flotta.controller;

import java.time.LocalDate;
import java.util.Optional;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.model.registry.Device;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.service.ServiceManager;
import com.flotta.utility.Utility;

@Controller
public class DeviceController {

  private ServiceManager service;

  @Autowired
  public void setMainService(ServiceManager service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "Device");
  }

  @RequestMapping("/device/all")
  public String listDevices(Model model) {
    model.addAttribute("canCreateNew", !service.findAllBrandOfDevicesType().isEmpty());
    model.addAttribute("devices", Utility.convertDevicesToView(service.findAllDevices()));
    return "device_templates/deviceAll";
  }
  
  @GetMapping("/device/new")
  public String prepareCreatingDevice(Model model) {
    model.addAttribute("device", new DeviceToView());
    model.addAttribute("deviceTypes", Utility.sortDeviceTypeByName(service.findAllVisibleDeviceTypes()));
    return "device_templates/deviceNew";
  }
  
  @PostMapping("/device/new")
  public String createDevice(Model model, @ModelAttribute("device") DeviceToView dtv) {
    if(service.createDevice(dtv)) {
      return "redirect:/device/all";
    } else {
      model.addAttribute("device", dtv);
      model.addAttribute("deviceTypes", Utility.sortDeviceTypeByName(service.findAllVisibleDeviceTypes()));
      model.addAttribute("error", service.getDeviceServiceError());
      return "device_templates/deviceNew";
    }
  }
  
  @GetMapping("/device/{id}/update")
  public String prepareUpdatingDevice(Model model, @PathVariable("id") long id) {
    Optional<Device> deviceOpt = service.findDeviceById(id);
    if(deviceOpt.isPresent()) {
      model.addAttribute("device", new DeviceToView(deviceOpt.get()));
      model.addAttribute("users", Utility.sortUserByName(service.findAllUser()));
      return "device_templates/deviceEdit";
    } else {
      return "redirect:/device/all";
    }
  }
  
  @PostMapping("/device/{id}/update")
  public String updateDevice(RedirectAttributes ra,  @ModelAttribute() DeviceToView dtv) {
    if(!service.updateDevice(dtv)) {
      ra.addFlashAttribute("error", service.getDeviceServiceError());
    }
    return "redirect:/device/" + dtv.getId() + "/update";
  }
  
  @GetMapping("/device/{id}/view")
  public String viewDevice(Model model, @PathVariable("id") long id) {
    Optional<Device> deviceOpt = service.findDeviceById(id);
    if(deviceOpt.isPresent()) {
      model.addAttribute("device", new DeviceToView(deviceOpt.get()));
      model.addAttribute("dates", deviceOpt.get().getAllModificationDateDesc());
      return "device_templates/deviceView";
    } else {
      return "redirect:/device/all";
    }
  }
  
  @PostMapping("/device/{id}/view")
  @ResponseBody
  public DeviceToView viewChangeDate(@PathVariable("id") long id, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate date) {
    return new DeviceToView(service.findDeviceById(id).get(), date);
  }
  
  
}
