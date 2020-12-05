package com.flotta.controller;

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

import com.flotta.enums.ControllerType;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.model.registry.User;
import com.flotta.service.MessageService;
import com.flotta.service.ServiceManager;
import com.flotta.utility.ExtendedBoolean;
import com.flotta.utility.Utility;

@Controller
public class ProfileController {

  private ServiceManager service;

  @Autowired
  private MessageService messageService;
  
  @Autowired
  public void setMainService(ServiceManager service) {
    this.service = service;
    messageService.setActualController(ControllerType.PROFILE);
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
    ExtendedBoolean eb = service.changePassword(getActualUserEmail(), params.get("old-password"), params.get("new-password"), params.get("confirm-new-password"));
    messageService.clearAndAddMessage(eb);
    return "profile/passwordChange";
  }
  
  @GetMapping("/profile/invoice")
  public String showActualUserPendingInvoices(Model model) {
    model.addAttribute("invoiceParts", service.findInvoicesOfUserByEmail(getActualUserEmail()));
    return "profile/invoiceSummary";
  }
  
  @PostMapping("/profile/invoice/accept")
  @ResponseBody
  public List<Long> acceptInvoicesOfCurrentUserByInvoiceIds(@RequestParam("ids") List<Long> ids) {
    service.acceptInvoicesOfUserByEmailAndIdsFromUser(getActualUserEmail(), ids);
    return ids;
  }

  @PostMapping("/profile/invoice/{id}")
  public String details(Model model, @PathVariable ("id") long id) {
    Optional<InvoiceByUserAndPhoneNumber> invoiceOpt = service.findInvoiceOfUserByEmailAndId(getActualUserEmail(), id);
    if(invoiceOpt.isPresent()) {
      model.addAttribute("invoicePart", invoiceOpt.get());
      return "profile/invoiceDetails";
    } else {
      messageService.clearAndAddMessage(MessageKey.UNKNOWN_INVOICE, MessageType.WARNING);
      return "redirect:/profile/invoice";
    }
  }
  
  @PostMapping("/profile/invoice/{id}/revision")
  public String createRevisionText(Model model, @PathVariable ("id") long id, @RequestParam Map<String, String> notes) {
    service.askForRevision(getActualUserEmail(), id, notes);
    return "redirect:/profile/invoice";
  }
  
  private String getActualUserEmail() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName();
  }
  
}