package com.flotta.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flotta.entity.viewEntity.DeviceToView;
import com.flotta.entity.viewEntity.SubscriptionToView;
import com.flotta.service.MainService;

@Controller
public class SubscriptionController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @ModelAttribute
  public void title(Model model) {
    model.addAttribute("title", "Subscription");
  }

  @GetMapping("/subscription/all")
  public String listSubscriptions(Model model) {
    model.addAttribute("canCreateNew", service.canCreateSubscription());
    model.addAttribute("subscriptions", service.findAllSubscription());
    return "subscription_templates/subscriptionAll";
  }

  @GetMapping("/subscription/new")
  public String prepareAddingSubscription(Model model) {
    if(service.canCreateSubscription()) {
      model.addAttribute("subscription", new SubscriptionToView());
      model.addAttribute("freeSims", service.findAllFreeSim());
      return "subscription_templates/subscriptionNew";
    } else {
      return "redirect:/subscription/all";
    }
  }

  @PostMapping("/subscription/new")
  public String addSubscription(Model model, @ModelAttribute("subscription") SubscriptionToView stv) {
      if (service.addSubscription(stv)) {
        return "redirect:/subscription/all";
      } else {
        model.addAttribute("subscription", stv);
        model.addAttribute("freeSims", service.findAllFreeSim());
        model.addAttribute("error", service.getSubscriptionServiceError());
        return "subscription_templates/subscriptionNew";
      }
  }

  @GetMapping("/subscription/{id}/update")
  public String prepareUpdatingSubscription(Model model, @PathVariable("id") long id) {
    SubscriptionToView stv = service.findSubscriptionById(id);
    model.addAttribute("subscription", stv);
    model.addAttribute("freeSims", service.findAllFreeSim());
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("devices", service.findAllDevicesByUser(stv.getUserId()));
    return "subscription_templates/subscriptionEdit";
  }

  @PostMapping("/subscription/{id}/update")
  public String updateSubscription(Model model, @ModelAttribute() SubscriptionToView stv) {
    if(!service.updateSubscription(stv)) {
      model.addAttribute("error", service.getSubscriptionServiceError());
    }
    stv = service.findSubscriptionById(stv.getId());
    model.addAttribute("freeSims", service.findAllFreeSim());
    model.addAttribute("users", service.findAllUser());
    model.addAttribute("devices", service.findAllDevicesByUser(stv.getUserId()));
    model.addAttribute("subscription", stv);
    return "subscription_templates/subscriptionEdit";
  }
  
  @GetMapping("/subscription/{id}/view")
  public String viewSubscription(Model model, @PathVariable("id") long id) {
    model.addAttribute("subscription", service.findSubscriptionById(id));
    model.addAttribute("dates", service.findSubscriptionDatesById(id));
    return "subscription_templates/subscriptionView";
  }
  
  @PostMapping("/subscription/{id}/view")
  @ResponseBody
  public SubscriptionToView viewChangeDate(@PathVariable("id") long id, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate date) {
    return service.findSubscriptionByIdAndDate(id, date);
  }
  
  @PostMapping("/subscription/getDevicesByUser")
  @ResponseBody
  public List<DeviceToView> getDevicesByUser(@RequestParam ("userId") long userId) {
    return service.findAllDevicesByUser(userId);
  }
  
  @PostMapping("/subscription/getDeviceById")
  @ResponseBody
  public DeviceToView getDeviceById(Model model, @RequestParam ("id") long id) {
    return service.findDeviceById(id);
  }
  
}
