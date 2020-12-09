package com.flotta.model.invoice;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;

@Entity
@Table(name = "invoices")
public class Invoice extends BasicInvoice {

  @ManyToOne
  private Participant company;

  private boolean acceptedByUsers;

  private boolean acceptedByCompany;

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
  private List<InvoiceByUserAndPhoneNumber> invoicePart = new LinkedList<>();

  public static final Comparator<Invoice> BY_INVOICE_NUMBER_ADN_ACCEPT_BY_COMPANY_AMD_DATE  = 
      Comparator.comparing(Invoice::isAcceptedByCompany)
      .thenComparing(Invoice::getBeginDate)
      .thenComparing(Invoice::getInvoiceNumber);
  
  public Invoice() {}

  public Participant getCompany() {
    return company;
  }

  public void setCompany(Participant company) {
    this.company = company;
  }

  public boolean isAcceptedByUsers() {
    return acceptedByUsers;
  }

  public void setAcceptedByUsers(boolean acceptedByUsers) {
    this.acceptedByUsers = acceptedByUsers;
  }

  public boolean isAcceptedByCompany() {
    return acceptedByCompany;
  }

  public void setAcceptedByCompany(boolean acceptedByCompany) {
    this.acceptedByCompany = acceptedByCompany;
  }

  public List<InvoiceByUserAndPhoneNumber> getInvoicePart() {
    return invoicePart;
  }

  public void setInvoicePart(List<InvoiceByUserAndPhoneNumber> invoicePart) {
    this.invoicePart = invoicePart;
  }

  // TODO átalakít
  public void processAndAddFeeItem(Subscription subscription, FeeItem fee) {
    fee.setCategory(company.getDescriptionCategoryCoupler().getCategoryByDescription(fee.getDescription()));
    List<FeeItem> feeItems = splitByUser(fee, subscription);
    processFeeItems(feeItems, subscription);
    for (FeeItem feeItem : feeItems) {
      boolean hasPart = false;
      for (InvoiceByUserAndPhoneNumber part : invoicePart) {
        if(part.getSubscription().equals(subscription) &&
            (feeItem.getUserId() == 0 && part.getUser() == null) || 
            (part.getUser() != null && feeItem.getUserId() == part.getUser().getId())) {
          part.addFeeItem(feeItem);
          hasPart = true;
          break;
        }
      }
      if (!hasPart) {
        InvoiceByUserAndPhoneNumber part = new InvoiceByUserAndPhoneNumber(this, subscription, subscription.getUserByDate(feeItem.getBeginDate()));
        part.addFeeItem(feeItem);
        invoicePart.add(part);
      }
    }
  }

  private List<FeeItem> splitByUser(FeeItem feeItem, Subscription subscription) {
    List<LocalDate> dates = subscription.getUserChangeDatesBetween(feeItem.getBeginDate(), feeItem.getEndDate());
    List<FeeItem> splittedFeeItems = feeItem.splitBeforeDate(dates);
    return splittedFeeItems;
  }
  
  private void processFeeItems(List<FeeItem> feeItems, Subscription subscription) {
    for(FeeItem feeItem : feeItems ) {
      User user = subscription.getUserByDate(feeItem.getBeginDate());
      if(user == null) {
        feeItem.setChargeRatioByUserPercentage(0);
        feeItem.setUserId(0);
      } else {
        int userRatio = user != null ? user.getChargeRatio().getRatioByCategory(feeItem.getCategory()) : 0;
        feeItem.setChargeRatioByUserPercentage(userRatio);
        feeItem.setUserId(user.getId());
      }
    }
  }

  public boolean canDelete() {
    return !acceptedByUsers && !acceptedByCompany;
  }

  public boolean isConsistant() {
    return true;
  }
  
  public void setAcceptedByCompany() {
    acceptedByCompany = true;
    int partOfCompany = 0;
    for (InvoiceByUserAndPhoneNumber part : invoicePart) {
      part.removeAllReviewNote();
      part.setAcceptedByCompany(true);
      if (part.getUser() == null || part.getUserGrossAmount() == 0) {
        part.setAcceptedByUser(true);
        partOfCompany++;
      }
    }
    if (partOfCompany == invoicePart.size()) {
      acceptedByUsers = true;
    }
  }

  public boolean hasAnyReviewNote() {
    for (InvoiceByUserAndPhoneNumber part : invoicePart) {
      if (part.hasReviewNote() || part.hasAnyReviewNoteOfFeeItems()) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return List<String> that contains all different descriptions from FeeItems
   */
  public List<String> getAllDescription() {
    Set<String> descriptions = new HashSet<>();
    for (InvoiceByUserAndPhoneNumber part : invoicePart) {
      descriptions.addAll(part.getAllDescription());
    }
    return new LinkedList<>(descriptions);
  }

}
