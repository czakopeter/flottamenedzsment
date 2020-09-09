package com.flotta.controller;


import java.util.LinkedList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.service.MainService;

@Controller
public class ProfileController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @GetMapping("/profile/items")
  public String listItems(Model model) {
    model.addAttribute("devices", service.findAllCurrentDeviceOfUser());
    model.addAttribute("subscriptions", service.findAllCurrentSubscriptionOfUser());
    return "profile/items";
  }
  
  @GetMapping("/profile/changePassword")
  public String preparePasswordChanging() {
    return "profile/passwordChange";
  }
  
  @PostMapping("/profile/changePassword")
  public String passwordChange(Model model, RedirectAttributes ra, @RequestParam Map<String, String> params) {
    if(service.changePassword(params.get("old-password"), params.get("new-password"), params.get("confirm-new-password"))) {
      ra.addFlashAttribute("success", "Change password was success");
      return "redirect:/logout";
    } else {
      model.addAttribute("error", service.getUserError());
      return "profile/passwordChange";
    }
  }
  
  @GetMapping("/profile/finance")
  public String showActualUserPendingInvoices(Model model) {
    model.addAttribute("invoicePerNumbers", service.getPendingInvoicesOfCurrentUser());
    return "profile/financeSummary";
  }
  
  @PostMapping("/profile/finance/{number}/accept")
  public String acceptInvoiceOfCurrentUserByNumber(@PathVariable("number") String number) {
    if(service.acceptInvoiceOfCurrentUserByNumber(number)) {
      System.out.println("Accepted");
    } else {
      System.out.println("Some problem");
    }
    return "redirect:/profile/finance";
  }
  
  @PostMapping("/profile/finance/accept")
  public String acceptInvoicesOfUserByNumbers(@RequestParam Map<String, String> accept) {
    if(accept != null) {
      if(service.acceptInvoicesOfCurrentUserByNumbers(new LinkedList<>(accept.keySet()))) {
        
      }
      
    }
    return "redirect:/profile/finance";
  }

  //TODO ellenőrizni h number és user kapcsolatban van e
  @PostMapping("/profile/finance/{number}")
  public String details(Model model, @PathVariable ("number") String number) {
    model.addAttribute("invoicePart", service.getPendingInvoiceOfCurrentUserByNumber(number));
    return "profile/financeDetails";
  }
  
  @PostMapping("/profile/finance/{number}/revision")
  public String createRevisionText(Model model, @PathVariable ("number") String number, @RequestParam Map<String, String> map) {
    service.askForRevision(number, map);
    model.addAttribute("invoicePart", service.getPendingInvoiceOfCurrentUserByNumber(number));
    return "redirect:/profile/finance";
  }
  
}