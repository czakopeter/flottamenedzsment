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

import com.flotta.invoice.ChargeRatioByCategory;
import com.flotta.invoice.Participant;
import com.flotta.service.MainService;

@Controller
public class InvoiceConfigureController {

  private static final String TEMPLATE_PATH = "/invoice_config_templates";
  
  private MainService service;
  
  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }
  
  @ModelAttribute
  private void title(Model model) {
  }
  
  @GetMapping("/finance/category/all")
  public String listCategories(Model model) {
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("add", new String());
    return TEMPLATE_PATH + "/category";
  }
  
  @PostMapping("/finance/category/all")
  public String addCategory(Model model, @ModelAttribute("add") String category) {
    if(service.addCategory(category)) {
    }
    return "redirect:" + TEMPLATE_PATH + "/category/all";
  }
  
  @GetMapping("finance/invoiceDescriptionCategoryCoupler/all")
  public String listInvoiceDescriptionCategoryCoupler(Model model) {
    model.addAttribute("couplers", service.findAllDescriptionCategoryCoupler());
    return TEMPLATE_PATH + "/invoiceDescriptionCategoryCouplerAll";
  }
  
  @GetMapping("/finance/invoiceDescriptionCategoryCoupler/{id}")
  public String prepareEditingInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    model.addAttribute("coupler", service.findBillPartitionTemplateById(id));
    model.addAttribute("categories", service.findAllCategory());
    return TEMPLATE_PATH + "/invoiceDescriptionCategoryCouplerEdit";
  }
  
  @PostMapping("/finance/invoiceDescriptionCategoryCoupler/{id}")
  public String editInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id, @RequestParam("description") List<String> descriptions, @RequestParam("category") List<Long> categories, @RequestParam(name = "available", defaultValue = "false") boolean available) {
    model.addAttribute("coupler", service.findBillPartitionTemplateById(id));
    model.addAttribute("categories", service.findAllCategory());
    service.upgradeDescriptionCategoryCoupler(id, descriptions, categories);
    return TEMPLATE_PATH + "/invoiceDescriptionCategoryCouplerEdit";
  }
  
  @GetMapping("/finance/invoiceDescriptionCategoryCoupler/{id}/view")
  public String viewInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    model.addAttribute("coupler", service.findBillPartitionTemplateById(id));
    return TEMPLATE_PATH + "/invoiceDescriptionCategoryCouplerView";
  }
  
  @GetMapping("finance/invoiceDescriptionCategoryCoupler/{id}/update")
  public String prepareUpdatingInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    model.addAttribute("id", id);
    model.addAttribute("descriptions", service.getUnknownFeeDescToTemplate(id));
    model.addAttribute("categories", service.findAllCategory());
    return TEMPLATE_PATH + "/invoiceDescriptionCategoryCouplerUpgrade";
  }
  
  @PostMapping("finance/invoiceDescriptionCategoryCoupler/{id}/update")
  public String updateInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id, @RequestParam("description") List<String> descriptions, @RequestParam("category") List<Long> categories) {
    service.upgradeDescriptionCategoryCoupler(id, descriptions, categories);
    return "redirect:" + TEMPLATE_PATH + "invoice/all";
  }
  
  @GetMapping("finance/chargeRatio/all")
  public String listChargeRatio(Model model) {
    model.addAttribute("chargeRatios", service.findAllChargeRatio());
    return TEMPLATE_PATH + "/chargeRatioByCategoryAll";
  }
  
  @GetMapping("/finance/chargeRatio/new")
  public String prepareAddingChargeRatio(Model model) {
    model.addAttribute("unusedCategories", service.findAllCategory());
    model.addAttribute("chargeRatio", new ChargeRatioByCategory());
    return TEMPLATE_PATH + "/chargeRatioByCategoryNew";
  }
  
  @PostMapping("/finance/chargeRatio/new")
  public String addChargeRatio(Model model, @ModelAttribute("chargeRatio") ChargeRatioByCategory chargeRatio, @RequestParam("category") List<Long> categories, @RequestParam("ratio") List<Integer> ratios) {
    if(service.addChargeRatio(chargeRatio, categories, ratios)) {
      return "redirect:/finance/chargeRatio/all";
    }
    model.addAttribute("unusedCategories", service.findAllCategory());
    return TEMPLATE_PATH + "/chargeRatioByCategoryNew";
  }
  
  @GetMapping("/finance/chargeRatio/{id}")
  public String prepareEditingChargeRatio(Model model, @PathVariable("id") long id) {
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("chargeRatio", service.findChargeRatioById(id));
    model.addAttribute("unusedCategories", service.getUnusedCategoryOfChargeRatio(id));
    return TEMPLATE_PATH + "/chargeRatioByCategoryEdit";
  }
  
  @PostMapping("/finance/chargeRatio/{id}")
  public String editChargeRatio(Model model, @PathVariable("id") long id, @RequestParam("category") List<Long> categories, @RequestParam("ratio") List<Integer> ratios) {
    if(service.editChargeRatio(id, categories, ratios)) {
    }
    model.addAttribute("chargeRatio", service.findChargeRatioById(id));
    model.addAttribute("categories", service.findAllCategory());
    return TEMPLATE_PATH + "/chargeRatioByCategoryEdit";
  }
  
  @GetMapping("/finance/participant/all")
  public String listParticipant(Model model) {
    model.addAttribute("participants", service.findAllParticipant());
    return TEMPLATE_PATH + "/participantAll";
  }
  
  @GetMapping("/finance/participant/{id}")
  public String prepareEditingParticipant(Model model, @PathVariable("id") long id) {
    Optional<Participant> participant = service.findParticipantById(id);
    if(participant.isPresent()) {
      model.addAttribute("participant", participant.get());
      model.addAttribute("descriptionCategoryCouplers", service.findAllDescriptionCategoryCoupler());
      return TEMPLATE_PATH + "/participantEdit";
    } else {
      return "redirect:" + TEMPLATE_PATH + "/participant/all";
    }
  }
  
  @GetMapping("/finance/participant/new")
  public String prepareAddingParticipant(Model model) {
    model.addAttribute("participant", new Participant());
    return TEMPLATE_PATH + "/participantNew";
  }
  
  @PostMapping("/finance/participant/new")
  public String addParticipant(Model model, @ModelAttribute Participant participant) {
    if(service.addParticipant(participant)) {
    }
    model.addAttribute("participant", participant);
    return TEMPLATE_PATH +  "/participantNew";
  }
  
  @GetMapping("/invoiceConfigure/main")
  public String invoiceConfigureMain(Model model) {
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("couplers", service.findAllDescriptionCategoryCoupler());
    model.addAttribute("chargeRatios", service.findAllChargeRatio());
    model.addAttribute("participants", service.findAllParticipant());
    return TEMPLATE_PATH + "/invoiceConfigures";
  }
  
}
