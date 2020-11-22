package com.flotta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.entity.invoice.Category;
import com.flotta.entity.invoice.ChargeRatioByCategory;
import com.flotta.entity.invoice.DescriptionCategoryCoupler;
import com.flotta.entity.invoice.Participant;
import com.flotta.entity.record.User;
import com.flotta.service.MainService;

@Controller
public class InvoiceConfigurationController {

  private static final String TEMPLATE_PATH = "/invoice_config_templates";
  
  private MainService service;
  
  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }
  
//  @ModelAttribute
//  private void title(Model model) {
//  }
  
  @GetMapping("/invoiceConfiguration/main")
  public String invoiceConfigureMain(Model model, @RequestParam Optional<String> active) {
    if(active.isPresent()) {
      model.addAttribute("active", active.get());
    }
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("couplers", service.findAllDescriptionCategoryCoupler());
    model.addAttribute("chargeRatios", service.findAllChargeRatio());
    model.addAttribute("participants", service.findAllParticipant());
    model.addAttribute("users", service.findAllUser());
    return TEMPLATE_PATH + "/invoiceConfiguration";
  }
  
  //TODO mapping-nél finance lecserélni invoiceConfiguration-re mindenhol
//  @GetMapping("/finance/category/all")
//  public String listCategories(Model model) {
//    model.addAttribute("categories", service.findAllCategory());
//    model.addAttribute("add", new String());
//    return TEMPLATE_PATH + "/category";
//  }
  
//  @PostMapping("/invoiceConfiguration/category/add")
//  public String addCategory(@ModelAttribute("add") String category) {
//    if(service.addOrModifyCategory(category)) {
//    }
//    return "redirect:/invoiceConfiguration/main?active=category";
//  }
  
  @PostMapping("/invoiceConfiguration/category/addOrModify")
  @ResponseBody
  public Category addOrModifyCategory(@RequestParam("id") long id, @RequestParam("name") String name) {
    return service.addOfMofifyCategory(id, name);
  }
  
//  @GetMapping("finance/invoiceDescriptionCategoryCoupler/all")
//  public String listInvoiceDescriptionCategoryCoupler(Model model) {
//    model.addAttribute("couplers", service.findAllDescriptionCategoryCoupler());
//    return TEMPLATE_PATH + "/invoiceDescriptionCategoryCouplerAll";
//  }
  
  @GetMapping("/invoiceConfiguration/descriptionCategoryCoupler/new")
  public String prepareAddingInvoiceDescriptionCategoryCoupler(Model model) {
    model.addAttribute("coupler", new DescriptionCategoryCoupler());
    return TEMPLATE_PATH + "/descriptionCategoryCouplerNew";
  }
  
  @PostMapping("/invoiceConfiguration/descriptionCategoryCoupler/new")
  public String addInvoiceDescriptionCategoryCoupler(Model model, @ModelAttribute DescriptionCategoryCoupler dcc) {
    if(service.addDescriptionCategoryCoupler(dcc)) {
      return "redirect:/invoiceConfiguration/main?active=description-category-coupler";
    }
    model.addAttribute("coupler", dcc);
    return TEMPLATE_PATH + "/descriptionCategoryCouplerNew";
  }
  
  @GetMapping("/invoiceConfiguration/descriptionCategoryCoupler/{id}")
  public String prepareEditingInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    DescriptionCategoryCoupler dcc = service.findDescriptionCategoryCouplerById(id);
    if(model.containsAttribute("descriptions")) {
      @SuppressWarnings("unchecked")
      List<String> descriptions = (List<String>)model.getAttribute("descriptions");
      descriptions.removeAll(dcc.getSortedDescriptions());
    }
    model.addAttribute("coupler", service.findDescriptionCategoryCouplerById(id));
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("invoices", service.findAllInvoice());
    return TEMPLATE_PATH + "/descriptionCategoryCouplerEdit";
  }
  
  @PostMapping("/invoiceConfiguration/descriptionCategoryCoupler/{id}/addDescriptionsOfInvoice")
  public String addDescriptionsOfInvoice(RedirectAttributes ra, @PathVariable("id") long id, @RequestParam("selectedInvoice") long invoiceId) {
    ra.addFlashAttribute("descriptions", service.findDescriptionsOfInvoiceById(invoiceId));
    return "redirect:/invoiceConfiguration/descriptionCategoryCoupler/" + id;
  }
  
  @PostMapping("/invoiceConfiguration/descriptionCategoryCoupler/{id}")
  public String editInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id, @RequestParam("description") List<String> descriptions, @RequestParam("category") List<Long> categories, @RequestParam(name = "available", defaultValue = "false") boolean available) {
    service.upgradeDescriptionCategoryCoupler(id, descriptions, categories, available);
    model.addAttribute("coupler", service.findDescriptionCategoryCouplerById(id));
    model.addAttribute("categories", service.findAllCategory());
    return TEMPLATE_PATH + "/descriptionCategoryCouplerEdit";
  }
  
  @GetMapping("/invoiceConfiguration/descriptionCategoryCoupler/{id}/view")
  public String viewInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    model.addAttribute("coupler", service.findDescriptionCategoryCouplerById(id));
    return TEMPLATE_PATH + "/descriptionCategoryCouplerView";
  }
  
//  @GetMapping("finance/invoiceDescriptionCategoryCoupler/{id}/update")
//  public String prepareUpdatingInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
//    model.addAttribute("id", id);
//    model.addAttribute("descriptions", service.getUnknownFeeDescToTemplate(id));
//    model.addAttribute("categories", service.findAllCategory());
//    return TEMPLATE_PATH + "/invoiceDescriptionCategoryCouplerUpgrade";
//  }
//  
//  @PostMapping("finance/invoiceDescriptionCategoryCoupler/{id}/update")
//  public String updateInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id, @RequestParam("description") List<String> descriptions, @RequestParam("category") List<Long> categories, @RequestParam(name = "available", defaultValue = "false") boolean available) {
//    service.upgradeDescriptionCategoryCoupler(id, descriptions, categories, available);
//    return "redirect:" + TEMPLATE_PATH + "invoice/all";
//  }
  
//  @GetMapping("finance/chargeRatio/all")
//  public String listChargeRatio(Model model) {
//    model.addAttribute("chargeRatios", service.findAllChargeRatio());
//    return TEMPLATE_PATH + "/chargeRatioByCategoryAll";
//  }
  
  @GetMapping("/invoiceConfiguration/chargeRatio/new")
  public String prepareAddingChargeRatio(Model model) {
    model.addAttribute("unusedCategories", service.findAllCategory());
    model.addAttribute("chargeRatio", new ChargeRatioByCategory());
    return TEMPLATE_PATH + "/chargeRatioByCategoryNew";
  }
  
  @PostMapping("/invoiceConfiguration/chargeRatio/new")
  public String addChargeRatio(Model model, @ModelAttribute("chargeRatio") ChargeRatioByCategory chargeRatio) {
    if(service.addChargeRatio(chargeRatio)) {
      return "redirect:/invoiceConfiguration/main?active=charge-ratio";
    }
    model.addAttribute("unusedCategories", service.findAllCategory());
    return TEMPLATE_PATH + "/chargeRatioByCategoryNew";
  }
  
  @GetMapping("/invoiceConfiguration/chargeRatio/{id}")
  public String prepareEditingChargeRatio(Model model, @PathVariable("id") long id) {
    Optional<ChargeRatioByCategory> optional = service.findChargeRatioById(id);
    if(optional.isPresent()) {
      model.addAttribute("chargeRatio", optional.get());
      model.addAttribute("categories", service.findAllCategory());
      model.addAttribute("unusedCategories", service.getUnusedCategoryOfChargeRatio(id));
      return TEMPLATE_PATH + "/chargeRatioByCategoryEdit";
    }
    return "redirect:/invoiceConfiguration/main?active=charge-ratio";
  }
  
  @PostMapping("/invoiceConfiguration/chargeRatio/{id}")
  public String editChargeRatio(Model model, @PathVariable("id") long id, @RequestParam("category") List<Long> categories, @RequestParam("ratio") List<Integer> ratios) {
    if(service.editChargeRatio(id, categories, ratios)) {
    }
    Optional<ChargeRatioByCategory> optional = service.findChargeRatioById(id);
    if(optional.isPresent()) {
      model.addAttribute("chargeRatio", optional.get());
      model.addAttribute("categories", service.findAllCategory());
      return TEMPLATE_PATH + "/chargeRatioByCategoryEdit";
    }
    return "redirect:/invoiceConfiguration/main?active=charge-ratio";
  }
  
  @PostMapping("/invoiceConfiguration/getChargeRatioOfUser")
  @ResponseBody
  public ChargeRatioByCategory getChargeRatioOfUser(@RequestParam ("userId") long id) {
    return service.findUserById(id).getChargeRatio();
  }
  
  @PostMapping("/invoiceConfiguration/modifyChargeRatioOfUser")
  @ResponseBody
  public User modifyChargeRatioOfUser(@RequestParam ("userId") long userId, @RequestParam long chargeRatioId) {
    return service.editChargeRatioOfUser(userId, chargeRatioId);
  }
  
//  @GetMapping("/finance/participant/all")
//  public String listParticipant(Model model) {
//    model.addAttribute("participants", service.findAllParticipant());
//    return TEMPLATE_PATH + "/participantAll";
//  }
  
  @GetMapping("/invoiceConfiguration/participant/new")
  public String prepareAddingParticipant(Model model) {
    model.addAttribute("participant", new Participant());
    model.addAttribute("descriptionCategoryCouplers", service.findAllDescriptionCategoryCoupler());
    return TEMPLATE_PATH + "/participantNew";
  }
  
  @PostMapping("/invoiceConfiguration/participant/new")
  public String addParticipant(Model model, @ModelAttribute Participant participant, @RequestParam ("descriptionCategoryCoupler") long dccId) {
    if(service.addParticipant(participant, dccId)) {
      return "redirect:/invoiceConfiguration/main?active=participant";
    }
    model.addAttribute("participant", participant);
    model.addAttribute("descriptionCategoryCouplers", service.findAllDescriptionCategoryCoupler());
    return TEMPLATE_PATH + "/participantNew";
  }
  
  @GetMapping("/invoiceConfiguration/participant/{id}")
  public String prepareEditingParticipant(Model model, @PathVariable("id") long id) {
    Optional<Participant> participant = service.findParticipantById(id);
    if(participant.isPresent()) {
      model.addAttribute("participant", participant.get());
      model.addAttribute("descriptionCategoryCouplers", service.findAllDescriptionCategoryCoupler());
      return TEMPLATE_PATH + "/participantEdit";
    } else {
      return "redirect:/invoiceConfiguration/main?active=participant";
    }
  }
  
  @PostMapping("/invoiceConfiguration/participant/{id}")
  public String editParticipant(Model model, @ModelAttribute("participant") Participant participant, @PathVariable("id") long id) {
//    Optional<Participant> participant = service.findParticipantById(id);
//    if(participant.isPresent()) {
//      model.addAttribute("participant", participant.get());
//      model.addAttribute("descriptionCategoryCouplers", service.findAllDescriptionCategoryCoupler());
//      return TEMPLATE_PATH + "/participantEdit";
//    } else {
      return "redirect:/invoiceConfiguration/main?active=participant";
//    }
  }
  
}
