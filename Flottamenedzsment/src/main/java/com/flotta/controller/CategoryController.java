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
public class CategoryController {

  private MainService service;
  
  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }
  
  @ModelAttribute
  private void title(Model model) {
    model.addAttribute("title", "Category");
  }
  
  @GetMapping("/finance/category/all")
  public String listCategories(Model model) {
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("add", new String());
//    model.addAttribute("users", service.findAllUser());
//    model.addAttribute("chargeRatioNames", service.findAllChargeRatio());
    return "finance_templates/category";
  }
  
//  @PostMapping("/finance/category/add")
  @PostMapping("/finance/category/all")
  public String addCategory(Model model, @ModelAttribute("add") String category) {
    if(service.addCategory(category)) {
    }
    return "redirect:/finance/category/all";
  }
  
  @GetMapping("finance/invoiceDescriptionCategoryCoupler/all")
  public String listInvoiceDescriptionCategoryCoupler(Model model) {
    model.addAttribute("couplers", service.findAllDescriptionCategoryCoupler());
    return "finance_templates/invoiceDescriptionCategoryCouplerAll";
  }
  
  @GetMapping("/finance/invoiceDescriptionCategoryCoupler/{id}")
  public String prepareEditingInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    model.addAttribute("coupler", service.findBillPartitionTemplateById(id));
    model.addAttribute("categories", service.findAllCategory());
    return "finance_templates/invoiceDescriptionCategoryCouplerEdit";
  }
  
  @PostMapping("/finance/invoiceDescriptionCategoryCoupler/{id}")
  public String editInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id, @RequestParam("description") List<String> descriptions, @RequestParam("category") List<Long> categories, @RequestParam(name = "available", defaultValue = "false") boolean available) {
    model.addAttribute("coupler", service.findBillPartitionTemplateById(id));
    model.addAttribute("categories", service.findAllCategory());
    service.upgradeDescriptionCategoryCoupler(id, descriptions, categories);
    return "finance_templates/invoiceDescriptionCategoryCouplerEdit";
  }
  
  @GetMapping("/finance/invoiceDescriptionCategoryCoupler/{id}/view")
  public String viewInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    model.addAttribute("coupler", service.findBillPartitionTemplateById(id));
    return "finance_templates/invoiceDescriptionCategoryCouplerView";
  }
  
  @GetMapping("finance/invoiceDescriptionCategoryCoupler/{id}/update")
  public String prepareUpdatingInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    model.addAttribute("id", id);
    model.addAttribute("descriptions", service.getUnknownFeeDescToTemplate(id));
    model.addAttribute("categories", service.findAllCategory());
    return "finance_templates/invoiceDescriptionCategoryCouplerUpgrade";
  }
  
  @PostMapping("finance/invoiceDescriptionCategoryCoupler/{id}/update")
  public String updateInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id, @RequestParam("description") List<String> descriptions, @RequestParam("category") List<Long> categories) {
    service.upgradeDescriptionCategoryCoupler(id, descriptions, categories);
    return "redirect:/invoice/all";
  }
  
  @GetMapping("finance/chargeRatio/all")
  public String listChargeRatio(Model model) {
    model.addAttribute("chargeRatios", service.findAllChargeRatio());
    return "/finance_templates/chargeRatioByCategoryAll";
  }
  
  @GetMapping("/finance/chargeRatio/new")
  public String prepareAddingChargeRatio(Model model) {
    model.addAttribute("unusedCategories", service.findAllCategory());
    model.addAttribute("chargeRatio", new ChargeRatioByCategory());
    return "finance_templates/chargeRatioByCategoryNew";
  }
  
  @PostMapping("/finance/chargeRatio/new")
  public String addChargeRatio(Model model, @ModelAttribute("chargeRatio") ChargeRatioByCategory chargeRatio, @RequestParam("category") List<Long> categories, @RequestParam("ratio") List<Integer> ratios) {
    if(service.addChargeRatio(chargeRatio, categories, ratios)) {
      return "redirect:/finance/chargeRatio/all";
    }
    model.addAttribute("unusedCategories", service.findAllCategory());
    return "finance_templates/chargeRatioByCategoryNew";
  }
  
  @GetMapping("/finance/chargeRatio/{id}")
  public String prepareEditingChargeRatio(Model model, @PathVariable("id") long id) {
    model.addAttribute("categories", service.findAllCategory());
    model.addAttribute("chargeRatio", service.findChargeRatioById(id));
    model.addAttribute("unusedCategories", service.getUnusedCategoryOfChargeRatio(id));
    return "finance_templates/chargeRatioByCategoryEdit";
  }
  
  @PostMapping("/finance/chargeRatio/{id}")
  public String editChargeRatio(Model model, @PathVariable("id") long id, @RequestParam("category") List<Long> categories, @RequestParam("ratio") List<Integer> ratios) {
    if(service.editChargeRatio(id, categories, ratios)) {
    }
    model.addAttribute("chargeRatio", service.findChargeRatioById(id));
    model.addAttribute("categories", service.findAllCategory());
    return "finance_templates/chargeRatioByCategoryEdit";
  }
  
  @GetMapping("/finance/participant/all")
  public String listParticipant(Model model) {
    model.addAttribute("participants", service.findAllParticipant());
    return "finance_templates/participantAll";
  }
  
  @GetMapping("/finance/participant/{id}")
  public String prepareEditingParticipant(Model model, @PathVariable("id") long id) {
    Optional<Participant> participant = service.findParticipantById(id);
    if(participant.isPresent()) {
      model.addAttribute("participant", participant.get());
      model.addAttribute("descriptionCategoryCouplers", service.findAllDescriptionCategoryCoupler());
      return "finance_templates/participantEdit";
    } else {
      return "redirect:/finance/participant/all";
    }
  }
  
}
