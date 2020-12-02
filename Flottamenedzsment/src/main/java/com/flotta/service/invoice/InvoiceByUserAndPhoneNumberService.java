package com.flotta.service.invoice;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.model.registry.User;
import com.flotta.repository.invoice.InvoiceByUserAndPhoneNumberRepository;

/**
 * @author CzP
 *
 */
@Service
public class InvoiceByUserAndPhoneNumberService {
  
  private InvoiceByUserAndPhoneNumberRepository invoiceByUserAndPhoneNumberRepository;
  
  @Autowired
  public void setInvoiceByUserAndPhoneNumberRepository(InvoiceByUserAndPhoneNumberRepository invoiceByUserAndPhoneNumberRepository) {
    this.invoiceByUserAndPhoneNumberRepository = invoiceByUserAndPhoneNumberRepository;
  }

  List<InvoiceByUserAndPhoneNumber> getPendingInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberRepository.findAllByUserAndAcceptedByCompanyTrueAndAcceptedByUserFalse(user);
  }
  
  Optional<InvoiceByUserAndPhoneNumber> findInvoiceOfUserById(User user, Long id) {
    return invoiceByUserAndPhoneNumberRepository.findByIdAndUser(id, user);
  }

  void acceptInvoicesOfUserByUserAndIds(User user, List<Long> ids) {
    List<InvoiceByUserAndPhoneNumber> invoices = invoiceByUserAndPhoneNumberRepository.findAllByIdAndUser(ids);
    for(InvoiceByUserAndPhoneNumber invoice : invoices) {
      invoice.setAcceptedByUser(true);
    }
    invoiceByUserAndPhoneNumberRepository.save(invoices);
  }

  public void askRevisionOfInvoiceByUser(User user, long id, Map<String, String> map) {
    Optional<InvoiceByUserAndPhoneNumber> optional = invoiceByUserAndPhoneNumberRepository.findByIdAndUser(id, user);
    optional.ifPresent(invoice -> {
      invoice.getInvoice().setAcceptedByCompany(false);
      invoice.setReviewNote(map.remove("textarea"));
      map.forEach((k,v) -> {
        invoice.setReviewNoteOfFeeItem(Long.parseLong(k), v);
      });
      invoice.setAcceptedByCompany(false);
      invoiceByUserAndPhoneNumberRepository.save(invoice);
    });
  }

//  public List<InvoiceByUserAndPhoneNumber> getAcceptedInvoicesOfUser(User user) {
//    return invoiceByUserAndPhoneNumberRepository.findAllByUserAndAcceptedByCompanyTrueAndAcceptedByUserTrue(user);
//  }
//
//  public InvoiceByUserAndPhoneNumber getAcceptedInvoiceOfUserById(User user, long id) {
//    return invoiceByUserAndPhoneNumberRepository.findByIdAndUserAndAcceptedByCompanyTrueAndAcceptedByUserTrue(id, user);
//  }
  
  
  /**
   * Visszaadja a felhasználóhoz tartózó számlatételeket, melyek a cég oldaláról már elfogadásra kerültek
   * A lista rendeztett, előre kerülnek a felhasználó által még nem elfogadottak, azon belül meg dátum szerint növekvő a sorrend
   * @param user
   * @return Felhasználóhoz tartózó számlatételeket listája
   */
  public List<InvoiceByUserAndPhoneNumber> findInvoicesOfUserByEmail(User user) {
    return invoiceByUserAndPhoneNumberRepository.findAllByUserAndAcceptedByCompanyTrueOrderByAcceptedByUserAscBeginDateAsc(user);
  }
}
