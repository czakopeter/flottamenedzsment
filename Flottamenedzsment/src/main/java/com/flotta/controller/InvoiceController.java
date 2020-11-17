package com.flotta.controller;

import java.util.List;

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

import com.flotta.exception.invoice.FileUploadException;
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
    model.addAttribute("rawInvoices", service.findAllRawInvoice());
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
  
//  @PostMapping("/invoice/getDescriptionsOfInvoice")
//  @ResponseBody
//  public List<String> getDescriptionsOfInvoice(@RequestParam ("id") long id) {
//    return service.findDescriptionsOfInvoiceById(id);
//  }
  
  /**
   * @param invoiceNumber
   */
  @PostMapping("/rawInvoice/{invoiceNumber}/delete")
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteRawInvoice(@PathVariable("invoiceNumber") String invoiceNumber) {
    service.deleteRawInvoiceByInvoiceNumber(invoiceNumber);
  }
  
}
 