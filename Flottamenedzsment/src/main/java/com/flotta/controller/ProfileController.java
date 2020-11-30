package com.flotta.controller;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.model.registry.User;
import com.flotta.service.MainService;
import com.flotta.utility.Utility;

@Controller
public class ProfileController {

  private MainService service;

  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }

  @GetMapping("/profile/subscriptionAndDevice")
  public String listItems(Model model) {
    Optional<User> userOpt = service.findUserByEmail(getActualUserEmail());
    
    model.addAttribute("devices", Utility.convertUserDevicesToView(userOpt.get()));
    model.addAttribute("subscriptions", Utility.convertUserSubscriptionsToView(userOpt.get()));
    return "profile/subscriptionAndDevice";
  }
  
  @GetMapping("/profile/changePassword")
  public String preparePasswordChanging() {
    return "profile/passwordChange";
  }
  
  @PostMapping("/profile/changePassword")
  public String passwordChange(Model model, RedirectAttributes ra, @RequestParam Map<String, String> params) {
    if(service.changePassword(getActualUserEmail(), params.get("old-password"), params.get("new-password"), params.get("confirm-new-password"))) {
      model.addAttribute("success", "Change password was success");
    } else {
      model.addAttribute("error", service.getUserError());
    }
    return "profile/passwordChange";
  }
  
  //TODO Felhasználóhoz kapcsolt összes visszaadása, javascript-ből szűrés rá(elfogadot vagy nem, min összeg stb.)
  @GetMapping("/profile/invoice")
  public String showActualUserPendingInvoices(Model model) {
    model.addAttribute("invoiceParts", service.getInvoicesOfUserByEmail(getActualUserEmail()));
    return "profile/invoiceSummary";
  }
  
  @PostMapping("/profile/invoice/accept")
  @ResponseBody
  public List<Long> acceptInvoicesOfCurrentUserByNumbers(@RequestParam("ids") List<Long> ids) {
    if(service.acceptInvoicesOfCurrentUserByInvoiceNumbersAndPhoneNumbers(getActualUserEmail(), ids)) {
      return ids;
    }
    return new LinkedList<>();
  }

  @PostMapping("/profile/invoice/{id}")
  public String details(Model model, @PathVariable ("id") long id) {
    model.addAttribute("invoicePart", service.getPendingInvoiceOfCurrentUserById(getActualUserEmail(), id));
    return "profile/invoiceDetails";
  }
  
  @PostMapping("/profile/invoice/{id}/revision")
  public String createRevisionText(Model model, @PathVariable ("id") long id, @RequestParam Map<String, String> map) {
    service.askForRevision(getActualUserEmail(), id, map);
    return "redirect:/profile/invoice";
  }
  
  private String getActualUserEmail() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName();
  }
  
}