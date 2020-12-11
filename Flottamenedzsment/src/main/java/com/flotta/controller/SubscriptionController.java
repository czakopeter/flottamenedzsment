package com.flotta.controller;

import java.time.LocalDate;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flotta.enums.ControllerType;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.registry.Subscription;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.service.MessageService;
import com.flotta.service.ServiceManager;
import com.flotta.utility.BooleanWithMessages;
import com.flotta.utility.Utility;

@Controller
public class SubscriptionController {
  @Autowired
  private ServiceManager service;
  
  @Autowired
  private MessageService messageService;

  @ModelAttribute
  public void prepareController(Model model) {
    model.addAttribute("title", "Subscription");
    model.addAttribute("locale", LocaleContextHolder.getLocale().getLanguage());
    messageService.setActualController(ControllerType.SUBSCRIPTION);
  }

  @GetMapping("/subscription/all")
  public String listSubscriptions(Model model) {
    model.addAttribute("subscriptions", Utility.convertSubscripionToView(service.findAllSubscription()));
    model.addAttribute("messages", messageService.getMessages());
    return "subscription_templates/subscriptionAll";
  }

  @GetMapping("/subscription/new")
  public String prepareCreatingSubscription(Model model) {
    BooleanWithMessages eb = service.canCreateSubscription();
    if(eb.booleanValue()) {
      model.addAttribute("subscription", new SubscriptionToView());
      model.addAttribute("freeSims", service.findAllFreeSim());
      model.addAttribute("messages", messageService.getMessages());
      return "subscription_templates/subscriptionNew";
    } else {
      messageService.addMessage(eb);
      return "redirect:/subscription/all";
    }
  }

  @PostMapping("/subscription/new")
  public String createSubscription(Model model, @ModelAttribute("subscription") SubscriptionToView stv) {
    BooleanWithMessages eb = service.createSubscription(stv); 
    messageService.addMessage(eb);
    model.addAttribute("messages", messageService.getMessages());
      if (eb.booleanValue()) {
        return "redirect:/subscription/all";
      } else {
        model.addAttribute("subscription", stv);
        model.addAttribute("freeSims", Utility.sortSimByImei(service.findAllFreeSim()));
        return "subscription_templates/subscriptionNew";
      }
  }

  @GetMapping("/subscription/{id}/update")
  public String prepareUpdatingSubscription(Model model, @PathVariable("id") long id) {
    Optional<Subscription> subscriptionOpt = service.findSubscriptionById(id);
    if(subscriptionOpt.isPresent()) {
      SubscriptionToView stv = new SubscriptionToView(subscriptionOpt.get());
      model.addAttribute("subscription", stv);
      model.addAttribute("freeSims", Utility.sortSimByImei(service.findAllFreeSim()));
      model.addAttribute("users", Utility.sortUserByName(service.findAllUser()));
      model.addAttribute("devices", Utility.convertDevicesToView(service.findAllCurrentDeviceByUser(stv.getUserId())));
      model.addAttribute("messages", messageService.getMessages());
      return "subscription_templates/subscriptionEdit";
    } else {
      messageService.addMessage(MessageKey.UNKNOWN_SUBSCRIPITON, MessageType.ERROR);
      return "redirect:/subscription/all";
    }
  }

  @PostMapping("/subscription/{id}/update")
  public String updateSubscription(@ModelAttribute() SubscriptionToView stv) {
    BooleanWithMessages eb = service.updateSubscription(stv);
    if(!eb.booleanValue()) {
      messageService.addMessage(eb);
    }
    return "redirect:/subscription/" + stv.getId() + "/update";
  }
  
  @GetMapping("/subscription/{id}/view")
  public String viewSubscription(Model model, @PathVariable("id") long id) {
    Optional<Subscription> subscriptionOpt = service.findSubscriptionById(id);
    if(subscriptionOpt.isPresent()) {
      model.addAttribute("subscription", new SubscriptionToView(subscriptionOpt.get()));
      model.addAttribute("dates", subscriptionOpt.get().getAllModificationDateDesc());
      return "subscription_templates/subscriptionView";
    } else {
      messageService.addMessage(MessageKey.UNKNOWN_SUBSCRIPITON, MessageType.ERROR);
    }
    return "redirect:/subscription/all";
  }
  
  @PostMapping("/subscription/{id}/view")
  @ResponseBody
  public SubscriptionToView viewDateChange(@PathVariable("id") long id, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate date) {
    return new SubscriptionToView(service.findSubscriptionById(id).get(), date);
  }
  
  @PostMapping("/subscription/getDevicesByUser")
  @ResponseBody
  public List<DeviceToView> getDevicesByUser(@RequestParam ("userId") long userId) {
    return Utility.convertDevicesToView(service.findAllCurrentDeviceByUser(userId));
  }
  
  @PostMapping("/subscription/getDeviceById")
  @ResponseBody
  public DeviceToView getDeviceById(@RequestParam ("id") long id) {
    return new DeviceToView(service.findDeviceById(id).get());
  }
}
