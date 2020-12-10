package com.flotta.service.invoice;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.invoice.GroupedFeeItems;
import com.flotta.model.registry.User;
import com.flotta.repository.invoice.GroupedFeeItemsRepository;

/**
 * @author CzP
 *
 */
@Service
public class GroupedFeeItemsService {
  
  private GroupedFeeItemsRepository groupedFeeItemsRepository;
  
  @Autowired
  public void setGroupedFeeItemsRepository(GroupedFeeItemsRepository groupedFeeItemsRepository) {
    this.groupedFeeItemsRepository = groupedFeeItemsRepository;
  }
  
  /**
   * Visszaadja a felhasználóhoz tartózó számlatételeket, melyek a cég oldaláról már elfogadásra kerültek
   * A lista rendeztett, előre kerülnek a felhasználó által még nem elfogadottak, azon belül meg dátum szerint növekvő a sorrend
   * @param user
   * @return Felhasználóhoz tartózó számlatételeket listája
   */
  public List<GroupedFeeItems> findAllByUser(User user) {
    return groupedFeeItemsRepository.findAllByUserAndAcceptedByCompanyTrueOrderByAcceptedByUserAscBeginDateAsc(user);
  }

  Optional<GroupedFeeItems> findByUserAndId(User user, Long id) {
    return groupedFeeItemsRepository.findByIdAndUser(id, user);
  }

  void acceptByUserAndIdsFromUser(User user, List<Long> ids) {
    List<GroupedFeeItems> invoices = new LinkedList<>();
    ids.forEach(id -> {
      groupedFeeItemsRepository.findByIdAndUser(id, user).ifPresent(invoice -> {
        invoice.acceptByUser();
        invoices.add(invoice);
      });
    });
    groupedFeeItemsRepository.save(invoices);
  }

  public void askForRevision(User user, long id, Map<String, String> notes) {
    Optional<GroupedFeeItems> optional = groupedFeeItemsRepository.findByIdAndUser(id, user);
    optional.ifPresent(invoice -> {
      invoice.getInvoice().setAcceptedByCompany(false);
      invoice.setAcceptedByCompany(false);
      invoice.setReviewNote(notes.remove("textarea"));
      notes.forEach((feeItemId, note) -> {
        invoice.setReviewNoteOfFeeItem(Long.parseLong(feeItemId), note);
      });
      groupedFeeItemsRepository.save(invoice);
    });
  }
}
