package com.flotta.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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

import com.flotta.enums.ControllerType;
import com.flotta.exception.invoice.FileUploadException;
import com.flotta.model.invoice.Invoice;
import com.flotta.service.MessageService;
import com.flotta.service.ServiceManager;
import com.flotta.utility.ResponseTransfer;
import com.flotta.utility.Utility;

@Controller
public class InvoiceController {

  private ServiceManager service;
  
  @Autowired
  private MessageService messageService;
  
  @Autowired
  public void setMainService(ServiceManager service) {
    this.service = service;
  }
  
  @ModelAttribute
  private void prepareController(Model model) {
    model.addAttribute("title", "Invoice");
    model.addAttribute("locale", LocaleContextHolder.getLocale().getCountry());
    messageService.setActualController(ControllerType.INVOICE);
  }
  
  @GetMapping("/invoice/all")
  public String listInvoces(Model model) {
    model.addAttribute("rawInvoices", Utility.sortRawInvoice(service.findAllRawInvoice()));
    model.addAttribute("invoices", Utility.sortInvoice(service.findAllInvoice()));
    return "invoice_templates/invoiceAll";
  }
  
  @PostMapping("/invoice/fileUpload")
  public String uploadInvoice(RedirectAttributes ra, @RequestParam("file") MultipartFile file) {
    try {
      service.fileUpload(file);
    } catch (FileUploadException e) {
      ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/invoice/all";
  }
  
  @GetMapping("invoice/{invoiceNumber}/details")
  public String invoiceDetails(Model model, @PathVariable("invoiceNumber") String invoiceNumber) {
    Optional<Invoice> invoice = service.findInvoiceByInvoiceNumber(invoiceNumber);
    if(invoice.isPresent()) {
      model.addAttribute("invoice", invoice.get());
      return "invoice_templates/invoiceDetails";
    } else {
      return "redirect:/invoice/all";
    }
  }
  
  @PostMapping("invoice/acceptByCompany")
  @ResponseBody
  public ResponseTransfer acceptInvoiceByCompany(@RequestParam("invoiceNumber") String invoiceNumber) {
    service.acceptInvoiceByInvoiceNumberFromCompany(invoiceNumber);
    return new ResponseTransfer(invoiceNumber);
  }
  
  @PostMapping("/invoice/delete")
  @ResponseBody
  public ResponseTransfer deleteInvoice(@RequestParam("invoiceNumber") String invoiceNumber) {
    service.deleteInvoiceByInvoiceNumber(invoiceNumber);
    return new ResponseTransfer(invoiceNumber);
  }
  
  @PostMapping("/invoice/modifyFeeItemGrossAmount")
  @ResponseStatus(value = HttpStatus.OK)
  public void modifyFeeItemAmountRatio(@RequestParam ("id") long id, @RequestParam ("userGrossAmount") double userAmount, @RequestParam ("compGrossAmount") double compAmount) {
    service.modifyFeeItemGrossAmountRatio(id, userAmount, compAmount);
  }
  
  @PostMapping("/rawInvoice/{invoiceNumber}/restartProcessing")
  public String restartProcessingRawInvoice(@PathVariable("invoiceNumber") String invoiceNumber) {
    service.restartProcessingRawInvoiceBy(invoiceNumber);
    return "redirect:/invoice/all";
  }
  
  /**
   * @param invoiceNumber
   */
  @PostMapping("/rawInvoice/delete")
  @ResponseBody
  public ResponseTransfer deleteRawInvoice(@RequestParam("invoiceNumber") String invoiceNumber) {
    service.deleteRawInvoiceByInvoiceNumber(invoiceNumber);
    return new ResponseTransfer(invoiceNumber);
  }
}
 