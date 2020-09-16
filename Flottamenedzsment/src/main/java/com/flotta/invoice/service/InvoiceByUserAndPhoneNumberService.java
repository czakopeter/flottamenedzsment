package com.flotta.invoice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.Subscription;
import com.flotta.entity.User;
import com.flotta.invoice.Invoice;
import com.flotta.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.invoice.repository.InvoiceByUserAndPhoneNumberRepository;

@Service
public class InvoiceByUserAndPhoneNumberService {
  
  private InvoiceByUserAndPhoneNumberRepository invoiceByUserAndPhoneNumberRepository;
  
  @Autowired
  public void setInvoiceByUserAndPhoneNumberRepository(InvoiceByUserAndPhoneNumberRepository invoiceByUserAndPhoneNumberRepository) {
    this.invoiceByUserAndPhoneNumberRepository = invoiceByUserAndPhoneNumberRepository;
  }

  public InvoiceByUserAndPhoneNumber getPendingInvoiceOfUserBySubscription(User user, Invoice invoice, Subscription subscription) {
    return invoiceByUserAndPhoneNumberRepository.findByUserAndInvoiceAndSubscriptionAndAcceptedByCompanyTrue(user, invoice, subscription);
  }

  public List<InvoiceByUserAndPhoneNumber> getPendingInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberRepository.findAllByUserAndAcceptedByCompanyTrueAndAcceptedByUserFalse(user);
  }
}
