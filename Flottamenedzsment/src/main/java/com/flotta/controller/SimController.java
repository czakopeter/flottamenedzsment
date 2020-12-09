package com.flotta.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.enums.ControllerType;
import com.flotta.model.registry.Sim;
import com.flotta.service.MessageService;
import com.flotta.service.ServiceManager;
import com.flotta.utility.ExtendedBoolean;

@Controller
public class SimController {

  private ServiceManager service;
  
  @Autowired
  private MessageService messageService;
  
  @Autowired
  public void setService(ServiceManager service) {
    this.service = service;
  }

  @ModelAttribute
  public void prepareController(Model model) {
    model.addAttribute("title", "Sim");
    model.addAttribute("locale", LocaleContextHolder.getLocale().getCountry());
    messageService.setActualController(ControllerType.SIM);
  }
  
  @GetMapping("/sim/all")
  public String listSims(Model model) {
    model.addAttribute("sims", service.findAllSim());
    model.addAttribute("messages", messageService.getMessages());
    return "sim_templates/simAll";
  }
  
  @RequestMapping("sim/new")
  public String prepareCreatingSim(Model model) {
    model.addAttribute("sim", new Sim());
    return "sim_templates/simNew";
  }
  
  @PostMapping("sim/new")
  public String createSim(Model model, RedirectAttributes ra, @ModelAttribute Sim sim) {
    ExtendedBoolean eb = service.createSim(sim); 
    messageService.addMessage(eb);
    if(eb.isValid()) {
      return "redirect:/sim/all";
    } else {
      model.addAttribute("sim", sim);
      model.addAttribute("messages", messageService.getMessages());
      return "sim_templates/simNew";
    }
  }
}