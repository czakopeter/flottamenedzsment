package com.flotta.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.model.registry.Subscription;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.service.MainService;
import com.flotta.utility.Utility;

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
    model.addAttribute("subscriptions", Utility.convertSubscripionToView(service.findAllSubscription()));
    return "subscription_templates/subscriptionAll";
  }

  @GetMapping("/subscription/new")
  public String prepareCreatingSubscription(Model model) {
    if(service.canCreateSubscription()) {
      model.addAttribute("subscription", new SubscriptionToView());
      model.addAttribute("freeSims", service.findAllFreeSim());
      return "subscription_templates/subscriptionNew";
    } else {
      return "redirect:/subscription/all";
    }
  }

  @PostMapping("/subscription/new")
  public String createSubscription(Model model, @ModelAttribute("subscription") SubscriptionToView stv) {
      if (service.createSubscription(stv)) {
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
    Optional<Subscription> subscriptionOpt = service.findSubscriptionById(id);
    if(subscriptionOpt.isPresent()) {
      SubscriptionToView stv = new SubscriptionToView(subscriptionOpt.get());
      model.addAttribute("subscription", stv);
      model.addAttribute("freeSims", service.findAllFreeSim());
      model.addAttribute("users", service.findAllUser());
      model.addAttribute("devices", Utility.convertDevicesToView(service.findAllCurrentDeviceByUser(stv.getUserId())));
      return "subscription_templates/subscriptionEdit";
    } else {
      return "redirect:/subscription/all";
    }
  }

  @PostMapping("/subscription/{id}/update")
  public String updateSubscription(RedirectAttributes ra, @ModelAttribute() SubscriptionToView stv) {
    if(!service.updateSubscription(stv)) {
      ra.addFlashAttribute("error", service.getSubscriptionServiceError());
    }
    return "redirect:/subscription/" + stv.getId() + "update";
  }
  
  @GetMapping("/subscription/{id}/view")
  public String viewSubscription(Model model, @PathVariable("id") long id) {
    Optional<Subscription> subscriptionOpt = service.findSubscriptionById(id);
    if(subscriptionOpt.isPresent()) {
      model.addAttribute("subscription", new SubscriptionToView(subscriptionOpt.get()));
      model.addAttribute("dates", subscriptionOpt.get().getAllModificationDateDesc());
      return "subscription_templates/subscriptionView";
    }
    return "redirect:/subscription/all";
  }
  
  @PostMapping("/subscription/{id}/view")
  @ResponseBody
  public SubscriptionToView viewChangeDate(@PathVariable("id") long id, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate date) {
    return new SubscriptionToView(service.findSubscriptionById(id).get(), date);
  }
  
  @PostMapping("/subscription/getDevicesByUser")
  @ResponseBody
  public List<DeviceToView> getDevicesByUser(@RequestParam ("userId") long userId) {
    return Utility.convertDevicesToView(service.findAllCurrentDeviceByUser(userId));
  }
  
  @PostMapping("/subscription/getDeviceById")
  @ResponseBody
  public DeviceToView getDeviceById(Model model, @RequestParam ("id") long id) {
    return new DeviceToView(service.findDeviceById(id).get());
  }
}
