package com.flotta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flotta.invoice.exception.FileUploadException;
import com.flotta.service.MainService;

@Controller
public class InvoiceController {

  private MainService service;
  
  @Autowired
  public void setMainService(MainService service) {
    this.service = service;
  }
  
  @ModelAttribute
  private void title(Model model) {
    model.addAttribute("title", "Invoice");
  }
  
  @GetMapping("/invoice/all")
  public String listInvoces(Model model) {
    model.addAttribute("invoices", service.findAllInvoice());
    return "invoice_templates/invoiceAll";
  }
  
  @PostMapping("/invoice/fileUpload")
  public String addInvoice(RedirectAttributes ra, @RequestParam("file") MultipartFile file) {
    try {
      service.fileUpload(file);
    } catch (FileUploadException e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/invoice/all";
  }
  
//TODO show full header of invoice
  @GetMapping("invoice/{invoiceNumber}/details")
  public String invoiceDetails(Model model, @PathVariable("invoiceNumber") String invoiceNumber) {
    model.addAttribute("invoice", service.findInvoiceByInvoiceNumber(invoiceNumber));
    return "invoice_templates/invoiceDetails";
  }
  
  @PostMapping("/invoice/{invoiceNumber}/restartProcessing")
  public String restartProcessingOfInvoice(@PathVariable(value = "invoiceNumber") String invoiceNumber) {
    service.restartPorcessingOfInvoice(invoiceNumber);
    return "redirect:/invoice/all";
  }
  
  @PostMapping("/invoice/{invoiceNumber}/delete")
  public String deleteInvoice(@PathVariable(value = "invoiceNumber") String invoiceNumber) {
    service.deleteInvoiceByInvoiceNumber(invoiceNumber);
    return "redirect:/invoice/all";
  }
  
  @PostMapping("invoice/{invoiceNumber}/accept")
  public String acceptInoiveByCompany(@PathVariable(value = "invoiceNumber") String invoiceNumber) {
    service.acceptInvoiceByInvoiceNumber(invoiceNumber);
    return "redirect:/invoice/all";
  }
  
  @PostMapping("/invoice/modifyFeeItemGrossAmountRatio")
  @ResponseStatus(value = HttpStatus.OK)
  public void modifyFeeItemAmountRatio(@RequestParam ("id") long id, @RequestParam ("userGrossAmount") double userAmount, @RequestParam ("compGrossAmount") double compAmount) {
    service.modifyFeeItemGrossAmountRatio(id, userAmount, compAmount);
  }
  
  
//  @PostMapping("/finance/invoicePartition")
//  public String billPartitionTemplate(Model model, @RequestParam(name = "bill_id") long billId, @RequestParam(name = "template_id") long templateId) {
//    if(service.billDivisionByTemplateId(billId, templateId)) {
//      return "redirect:/billing/all";
//      model.addAttribute("userFeeMap", service.splitting);
//      return "billing_templates/splittedBill";
//    } else {
//      model.addAttribute("templateId", templateId);
//      model.addAttribute("feeDescriptions", service.getUnknownFeeDescToTemplate(templateId));
//      model.addAttribute("categories", service.findAllCategory());
//      return "billing_templates/invoiceDescriptionCategoryCouplerUpgrade";
//    }
//  }
//  
//  @PostMapping("billing/billPartitionUpdate")
//  public String billPartitionTemplate(Model model, @RequestParam long templateId, @RequestParam(name = "description") List<String> descriptions, @RequestParam(name = "category") List<Long> categories) {
//    System.out.println("billing/billPartitionUpdate");
//    service.upgradeBillPartitionTemplate(templateId, descriptions, categories);
//    return "redirect:/billing/all";
//  }
  
  @GetMapping("/invoice/settings")
  public String invoicePartitionSettings(Model model) {
    model.addAttribute("categories", service.findAllCategory());
    return "finance_templates/financeSettings";
  }
}
 