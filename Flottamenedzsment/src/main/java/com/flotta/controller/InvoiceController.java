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

import com.flotta.enums.ControllerType;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.invoice.Invoice;
import com.flotta.service.MessageService;
import com.flotta.service.ServiceManager;
import com.flotta.utility.BooleanWithMessages;
import com.flotta.utility.ResponseTransfer;
import com.flotta.utility.Utility;

@Controller
public class InvoiceController {

  @Autowired
  private ServiceManager service;
  
  @Autowired
  private MessageService messageService;
  
  @ModelAttribute
  private void prepareController(Model model) {
    model.addAttribute("title", "Invoice");
    model.addAttribute("locale", LocaleContextHolder.getLocale().getLanguage());
    messageService.setActualController(ControllerType.INVOICE);
  }
  
  @GetMapping("/invoice/all")
  public String listInvoices(Model model) {
    model.addAttribute("rawInvoices", Utility.sortRawInvoice(service.findAllRawInvoice()));
    model.addAttribute("invoices", Utility.sortInvoice(service.findAllInvoice()));
    model.addAttribute("messages", messageService.getMessages());
    return "invoice_templates/invoiceAll";
  }
  
  @PostMapping("/invoice/fileUpload")
  public String uploadInvoice(@RequestParam("file") MultipartFile file) {
    if(file.getOriginalFilename().isEmpty()) {
      messageService.addMessage(MessageKey.FILE_NOT_CHOSE, MessageType.WARNING);
    } else {
      BooleanWithMessages eb = service.fileUpload(file);
      if(!eb.booleanValue()) {
        messageService.addMessage(eb);
      }
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
      messageService.addMessage(MessageKey.UNKNOWN_INVOICE, MessageType.ERROR);
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
  public void modifyFeeItemAmountRatio(@RequestParam ("id") long id, @RequestParam ("userGrossAmount") double userAmount) {
    service.modifyFeeItemGrossAmountRatio(id, userAmount);
  }
  
  @PostMapping("/rawInvoice/{invoiceNumber}/restartProcessing")
  public String restartProcessingOfRawInvoice(@PathVariable("invoiceNumber") String invoiceNumber) {
    service.restartProcessingRawInvoiceBy(invoiceNumber);
    return "redirect:/invoice/all";
  }
  
  @PostMapping("/rawInvoice/delete")
  @ResponseBody
  public ResponseTransfer deleteRawInvoice(@RequestParam("invoiceNumber") String invoiceNumber) {
    service.deleteRawInvoiceByInvoiceNumber(invoiceNumber);
    return new ResponseTransfer(invoiceNumber);
  }
}
 