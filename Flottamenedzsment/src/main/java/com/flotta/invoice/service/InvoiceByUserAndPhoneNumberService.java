package com.flotta.invoice.service;

import java.util.List;
import java.util.Optional;

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

  public boolean acceptInvoicesOfUserByInvoiceNumbersAndSubscription(User user, List<Long> ids) {
    for(Long id : ids) {
      Optional<InvoiceByUserAndPhoneNumber> optional = invoiceByUserAndPhoneNumberRepository.findById(id);
      if(optional.isPresent()) {
        InvoiceByUserAndPhoneNumber part = optional.get();
        part.setAcceptedByUser(true);
        invoiceByUserAndPhoneNumberRepository.save(part);
      }
    }
    return true;
  }
}
