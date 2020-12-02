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

import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.model.invoice.Participant;
import com.flotta.service.ServiceManager;
import com.flotta.utility.ResponseTransfer;



@Controller
public class InvoiceConfigurationController {

  private static final String TEMPLATE_PATH = "invoice_config_templates";
  
  private ServiceManager service;
  
  @Autowired
  public void setMainService(ServiceManager service) {
    this.service = service;
  }  
  
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
    return "invoice_config_templates/invoiceConfiguration";
  }
  
  @PostMapping("/invoiceConfiguration/category/addOrModify")
  @ResponseBody
  public Category createOrModifyCategory(@RequestParam("id") long id, @RequestParam("name") String name) {
    return service.createOrModifyCategory(id, name);
  }
  
  @GetMapping("/invoiceConfiguration/descriptionCategoryCoupler/new")
  public String prepareCreatingInvoiceDescriptionCategoryCoupler(Model model) {
    model.addAttribute("coupler", new DescriptionCategoryCoupler());
    return TEMPLATE_PATH + "/descriptionCategoryCouplerNew";
  }
  
  @PostMapping("/invoiceConfiguration/descriptionCategoryCoupler/new")
  public String createInvoiceDescriptionCategoryCoupler(Model model, @ModelAttribute DescriptionCategoryCoupler dcc) {
    if(service.createDescriptionCategoryCoupler(dcc)) {
      return "redirect:/invoiceConfiguration/main?active=description-category-coupler";
    }
    model.addAttribute("coupler", dcc);
    return TEMPLATE_PATH + "/descriptionCategoryCouplerNew";
  }
  
  @GetMapping("/invoiceConfiguration/descriptionCategoryCoupler/{id}")
  public String prepareUpdatingInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    Optional<DescriptionCategoryCoupler> dccOpt = service.findDescriptionCategoryCouplerById(id);
    if(dccOpt.isPresent()) {
      if(model.containsAttribute("descriptions")) {
        @SuppressWarnings("unchecked")
        List<String> descriptions = (List<String>)model.getAttribute("descriptions");
        descriptions.removeAll(dccOpt.get().getDescriptions());
      }
      model.addAttribute("coupler", dccOpt.get());
      model.addAttribute("categories", service.findAllCategory());
      model.addAttribute("invoices", service.findAllInvoice());
      model.addAttribute("rawInvoices", service.findAllRawInvoice());
      return TEMPLATE_PATH + "/descriptionCategoryCouplerEdit";
    } else {
      return "redirect:/invoiceConfiguration/main?active=description-category-coupler";
    }
    
  }
  
  @PostMapping("/invoiceConfiguration/descriptionCategoryCoupler/{id}/addDescriptionsOfInvoice")
  public String addDescriptionsOfInvoice(RedirectAttributes ra, @PathVariable("id") long id, @RequestParam("selectedInvoice") String invoiceNumber) {
    ra.addFlashAttribute("descriptions", service.findDescriptionsOfInvoiceByInvoiceNumber(invoiceNumber));
    return "redirect:/invoiceConfiguration/descriptionCategoryCoupler/" + id;
  }
  
  @PostMapping("/invoiceConfiguration/descriptionCategoryCoupler/{id}")
  public String updateInvoiceDescriptionCategoryCoupler(
      Model model, 
      @PathVariable("id") long id, 
      @RequestParam(name = "description") Optional<List<String>> descriptions,
      @RequestParam(name = "category") Optional<List<Long>> categories, 
      @RequestParam(name = "available", defaultValue = "false") boolean available) {
    if(descriptions.isPresent() && categories.isPresent()) {
      service.updateDescriptionCategoryCoupler(id, descriptions.get(), categories.get(), available);
    }
    return "redirect:/invoiceConfiguration/descriptionCategoryCoupler/" + id;
  }
  
  @GetMapping("/invoiceConfiguration/descriptionCategoryCoupler/{id}/view")
  public String viewInvoiceDescriptionCategoryCoupler(Model model, @PathVariable("id") long id) {
    Optional<DescriptionCategoryCoupler> dccOpt = service.findDescriptionCategoryCouplerById(id);
    if(dccOpt.isPresent()) {
      model.addAttribute("coupler", dccOpt.get());
      return TEMPLATE_PATH + "/descriptionCategoryCouplerView";
    } else {
      return "redirect:/invoiceConfiguration/main?active=description-category-coupler";
    }
  }
  
  @GetMapping("/invoiceConfiguration/chargeRatio/new")
  public String prepareCreatingChargeRatio(Model model) {
    model.addAttribute("chargeRatio", new ChargeRatioByCategory());
    return TEMPLATE_PATH + "/chargeRatioByCategoryNew";
  }
  
  @PostMapping("/invoiceConfiguration/chargeRatio/new")
  public String createChargeRatio(Model model, @ModelAttribute("chargeRatio") ChargeRatioByCategory chargeRatio) {
    if(service.createChargeRatio(chargeRatio)) {
      return "redirect:/invoiceConfiguration/main?active=charge-ratio";
    } else {
      model.addAttribute("chargeRatio", chargeRatio);
      return TEMPLATE_PATH + "/chargeRatioByCategoryNew";
    }
  }
  
  @GetMapping("/invoiceConfiguration/chargeRatio/{id}")
  public String prepareUpdatingChargeRatio(Model model, @PathVariable("id") long id) {
    Optional<ChargeRatioByCategory> chargeRatioOpt = service.findChargeRatioById(id);
    if(chargeRatioOpt.isPresent()) {
      model.addAttribute("chargeRatio", chargeRatioOpt.get());
      model.addAttribute("unusedCategories", service.findAllUnusedCategoryOfChargeRatio(id));
      return TEMPLATE_PATH + "/chargeRatioByCategoryEdit";
    } else {
      return "redirect:/invoiceConfiguration/main?active=charge-ratio";
    }
  }
  
  @PostMapping("/invoiceConfiguration/chargeRatio/{id}")
  public String updateChargeRatio(Model model, @PathVariable("id") long id, @RequestParam("category") List<Long> categories, @RequestParam("ratio") List<Integer> ratios) {
    service.updateChargeRatio(id, categories, ratios);
    return "redirect:/invoiceConfiguration/chargeRatio/" + id;
  }
  
  @PostMapping("/invoiceConfiguration/getChargeRatioOfUser")
  @ResponseBody
  public ResponseTransfer getChargeRatioIdOfUser(@RequestParam ("userId") long id) {
    Optional<ChargeRatioByCategory> chargeRatioOpt = service.getChargeRatioOfUserById(id);
    long chargeRatioId = chargeRatioOpt.isPresent() ? chargeRatioOpt.get().getId() : 0;
    return new ResponseTransfer(chargeRatioId);
  }
  
  @PostMapping("/invoiceConfiguration/modifyChargeRatioOfUser")
  @ResponseBody
  public ResponseTransfer updateChargeRatioOfUser(@RequestParam ("userId") long userId, @RequestParam long chargeRatioId) {
    return new ResponseTransfer(service.updateChargeRatioOfUser(userId, chargeRatioId) ? chargeRatioId : 0);
  }
  
  @GetMapping("/invoiceConfiguration/participant/new")
  public String prepareCreatingParticipant(Model model) {
    model.addAttribute("participant", new Participant());
    model.addAttribute("descriptionCategoryCouplers", service.findAllDescriptionCategoryCoupler());
    return TEMPLATE_PATH + "/participantNew";
  }
  
  @PostMapping("/invoiceConfiguration/participant/new")
  public String createParticipant(Model model, @ModelAttribute Participant participant) {
    if(service.createParticipant(participant)) {
      return "redirect:/invoiceConfiguration/main?active=participant";
    } else {
      model.addAttribute("participant", participant);
      model.addAttribute("descriptionCategoryCouplers", service.findAllDescriptionCategoryCoupler());
      return TEMPLATE_PATH + "/participantNew";
    }
  }
  
  @GetMapping("/invoiceConfiguration/participant/{id}")
  public String prepareUpdatingParticipant(Model model, @PathVariable("id") long id) {
    Optional<Participant> participantOpt = service.findParticipantById(id);
    if(participantOpt.isPresent()) {
      model.addAttribute("participant", participantOpt.get());
      model.addAttribute("descriptionCategoryCouplers", service.findAllDescriptionCategoryCoupler());
      return TEMPLATE_PATH + "/participantEdit";
    } else {
      return "redirect:/invoiceConfiguration/main?active=participant";
    }
  }
  
  @PostMapping("/invoiceConfiguration/participant/{id}")
  public String updateParticipant(Model model, @PathVariable("id") long id, @ModelAttribute("participant") Participant participant) {
    service.updateParticipant(participant);
    return "redirect:/invoiceConfiguration/participant/" + id;
  }
}