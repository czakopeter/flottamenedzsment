package com.flotta.invoice.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.Subscription;
import com.flotta.entity.User;
import com.flotta.invoice.Invoice;
import com.flotta.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.invoice.repository.InvoiceByUserAndPhoneNumberRepository;
import com.flotta.utility.Utility;

@Service
public class InvoiceByUserAndPhoneNumberService {
  
  private InvoiceByUserAndPhoneNumberRepository invoiceByUserAndPhoneNumberRepository;
  
  @Autowired
  public void setInvoiceByUserAndPhoneNumberRepository(InvoiceByUserAndPhoneNumberRepository invoiceByUserAndPhoneNumberRepository) {
    this.invoiceByUserAndPhoneNumberRepository = invoiceByUserAndPhoneNumberRepository;
  }

  //TODO null helyett Exception-t dobni, nincs ilyen id vagy nem a bejelentkezett felhaszáló-é
  public InvoiceByUserAndPhoneNumber getPendingInvoiceOfUserById(User user, Long id) {
    Optional<InvoiceByUserAndPhoneNumber> optional = invoiceByUserAndPhoneNumberRepository.findByIdAndUser(id, user);
    if(optional.isPresent()) {
      return optional.get();
    }
    return null;
  }

  public List<InvoiceByUserAndPhoneNumber> getPendingInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberRepository.findAllByUserAndAcceptedByCompanyTrueAndAcceptedByUserFalse(user);
  }

  public boolean acceptInvoicesOfUserByInvoiceNumbersAndSubscription(User user, List<Long> ids) {
    for(Long id : ids) {
      Optional<InvoiceByUserAndPhoneNumber> optional = invoiceByUserAndPhoneNumberRepository.findByIdAndUser(id, user);
      if(optional.isPresent()) {
        InvoiceByUserAndPhoneNumber part = optional.get();
        part.setAcceptedByUser(true);
        invoiceByUserAndPhoneNumberRepository.save(part);
      }
    }
    return true;
  }

  public void askForRevision(User user, long id, Map<String, String> map) {
    Optional<InvoiceByUserAndPhoneNumber> optional = invoiceByUserAndPhoneNumberRepository.findByIdAndUser(id, user);
    if(optional.isPresent()) {
      InvoiceByUserAndPhoneNumber part = optional.get();
      part.getInvoice().setAcceptedByCompany(false);
      part.setRevisionNote(map.remove("textarea"));
      map.forEach((k,v) -> {
        part.setRevisionNoteOfFeeItem(Long.parseLong(k), v);
      });
      part.setAcceptedByCompany(false);
      invoiceByUserAndPhoneNumberRepository.save(part);
    }
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberRepository.findAllByUserAndAcceptedByCompanyTrueAndAcceptedByUserTrue(user);
  }

  public InvoiceByUserAndPhoneNumber getAcceptedInvoiceOfUserById(User user, long id) {
    return invoiceByUserAndPhoneNumberRepository.findByIdAndUserAndAcceptedByCompanyTrueAndAcceptedByUserTrue(id, user);
  }

  public List<InvoiceByUserAndPhoneNumber> getAcceptedByCompanyInvoicesOfUser(User user) {
//    return invoiceByUserAndPhoneNumberRepository.findAllByUser(user);
    return invoiceByUserAndPhoneNumberRepository.findAllByUserAndAcceptedByCompanyTrueOrderByAcceptedByUserAscBeginDateAsc(user);
  }
}
