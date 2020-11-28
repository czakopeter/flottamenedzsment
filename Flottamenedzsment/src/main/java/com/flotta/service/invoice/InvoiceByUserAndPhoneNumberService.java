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
      part.setReviewNote(map.remove("textarea"));
      map.forEach((k,v) -> {
        part.setReviewNoteOfFeeItem(Long.parseLong(k), v);
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
  
  
  /**
   * Visszaadja a felhasználóhoz tartózó számlatételeket, melyek a cég oldaláról már elfogadásra kerültek
   * A lista rendeztett, előre kerülnek a felhasználó által még nem elfogadottak, azon belül meg dátum szerint növekvő a sorrend
   * @param user
   * @return Felhasználóhoz tartózó számlatételeket listája
   */
  public List<InvoiceByUserAndPhoneNumber> getAcceptedByCompanyInvoicesOfUser(User user) {
    return invoiceByUserAndPhoneNumberRepository.findAllByUserAndAcceptedByCompanyTrueOrderByAcceptedByUserAscBeginDateAsc(user);
  }
}
