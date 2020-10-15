package com.flotta.controller;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
  
  //TODO Felhasználóhoz kapcsolt összes visszaadása, javascript-ből szűrés rá(elfogadot vagy nem, min összeg stb.)
  @GetMapping("/profile/finance")
  public String showActualUserPendingInvoices(Model model) {
    model.addAttribute("invoiceParts", service.getAcceptedByCompanyInvoicesOfCurrentUser());
    return "profile/financeSummary";
  }
  
  @PostMapping("/profile/finance/accept")
  @ResponseBody
  public List<Long> acceptInvoicesOfCurrentUserByNumbers(@RequestParam("ids") List<Long> ids) {
    if(service.acceptInvoicesOfCurrentUserByInvoiceNumbersAndPhoneNumbers(ids)) {
      return ids;
    }
    return new LinkedList<>();
  }

  @PostMapping("/profile/finance/{id}")
  public String details(Model model, @PathVariable ("id") long id) {
    model.addAttribute("invoicePart", service.getPendingInvoiceOfCurrentUserById(id));
    return "profile/financeDetails";
  }
  
  @PostMapping("/profile/finance/{id}/revision")
  public String createRevisionText(Model model, @PathVariable ("id") long id, @RequestParam Map<String, String> map) {
    service.askForRevision(id, map);
    return "redirect:/profile/finance";
  }
  
//  @GetMapping("/profile/finance/history")
//  public String listAcceptedInvoiceOfCurrentUser(Model model) {
//    model.addAttribute("invoiceParts", service.getAcceptedInvoicesOfCurrentUser());
//    return "profile/acceptedInvoices";
//  }
//  
//  @PostMapping("/profile/finance/{id}/view")
//  public String detailsOfOneAcceptedInvoice(Model model, @PathVariable ("id") long id) {
//    model.addAttribute("invoicePart", service.getAcceptedInvoiceOfCurrentUserById(id));
//    return "profile/financeDetails";
//  }
  
}