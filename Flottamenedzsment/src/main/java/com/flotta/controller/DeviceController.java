package com.flotta.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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

import com.flotta.enums.Availability;
import com.flotta.enums.ControllerType;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.registry.Device;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.service.MessageService;
import com.flotta.service.ServiceManager;
import com.flotta.utility.ExtendedBoolean;
import com.flotta.utility.Utility;

@Controller
public class DeviceController {

  @Autowired
  private ServiceManager service;
  
  @Autowired
  private MessageService messageService;
  
  @ModelAttribute
  public void prepareController(Model model) {
    model.addAttribute("title", "Device");
    model.addAttribute("locale", LocaleContextHolder.getLocale().getCountry());
    messageService.setActualController(ControllerType.DEVICE);
  }

  @RequestMapping("/device/all")
  public String listDevices(Model model) {
    model.addAttribute("devices", Utility.convertDevicesToView(service.findAllDevices()));
    model.addAttribute("messages", messageService.getMessages());
    return "device_templates/deviceAll";
  }
  
  @GetMapping("/device/new")
  public String prepareCreatingDevice(Model model, RedirectAttributes ra) {
    ExtendedBoolean eb = service.canCreateDevice();
    if(eb.isValid()) {
      model.addAttribute("device", new DeviceToView());
      model.addAttribute("deviceTypes", Utility.sortDeviceTypeByName(service.findAllDeviceTypesByAvailability(Availability.AVAILABLE)));
      return "device_templates/deviceNew";
    } else {
      messageService.clearAndAddMessage(eb);
      return "redirect:/device/all";
    }
  }
  
  @PostMapping("/device/new")
  public String createDevice(Model model, @ModelAttribute("device") DeviceToView dtv) {
    ExtendedBoolean eb = service.createDevice(dtv);
    messageService.clearAndAddMessage(eb);
    if(eb.isValid()) {
      return "redirect:/device/all";
    } else {
      model.addAttribute("device", dtv);
      model.addAttribute("deviceTypes", Utility.sortDeviceTypeByName(service.findAllDeviceTypesByAvailability(Availability.AVAILABLE)));
      return "device_templates/deviceNew";
    }
  }
  
  @GetMapping("/device/{id}/update")
  public String prepareUpdatingDevice(Model model, RedirectAttributes ra , @PathVariable("id") long id) {
    Optional<Device> deviceOpt = service.findDeviceById(id);
    if(deviceOpt.isPresent()) {
      model.addAttribute("device", new DeviceToView(deviceOpt.get()));
      model.addAttribute("users", Utility.sortUserByName(service.findAllUser()));
      return "device_templates/deviceEdit";
    } else {
      messageService.clearAndAddMessage(MessageKey.UNKNOWN_DEVICE, MessageType.WARNING);
      return "redirect:/device/all";
    }
  }
  
  @PostMapping("/device/{id}/update")
  public String updateDevice(@ModelAttribute() DeviceToView dtv) {
    service.updateDevice(dtv); 
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
      messageService.clearAndAddMessage(MessageKey.UNKNOWN_DEVICE, MessageType.WARNING);
      return "redirect:/device/all";
    }
  }
  
  @PostMapping("/device/{id}/view")
  @ResponseBody
  public DeviceToView viewChangeDate(@PathVariable("id") long id, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate date) {
    return new DeviceToView(service.findDeviceById(id).get(), date);
  }
  
  
}
