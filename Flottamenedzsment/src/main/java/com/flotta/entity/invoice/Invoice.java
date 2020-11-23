package com.flotta.entity.invoice;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.entity.record.Subscription;
import com.flotta.entity.record.User;
import com.flotta.utility.Utility;

@Entity
@Table(name = "invoices")
public class Invoice {

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne
  private Participant company;

  private LocalDate beginDate;

  private LocalDate endDate;

  private String invoiceNumber;

  private double invoiceNetAmount;

  private double invoiceTaxAmount;

  private double invoiceGrossAmount;

  private boolean acceptedByUsers;

  private boolean acceptedByCompany;

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
  private List<InvoiceByUserAndPhoneNumber> invoicePart = new LinkedList<>();

  public Invoice() {
  }

  public Invoice(LocalDate fromDate, LocalDate endDate, String invoiceNumber, Double invoiceNetAmount, Double invoiceTaxAmount, double invoiceGrossAmount) {
    this.beginDate = fromDate;
    this.endDate = endDate;
    this.invoiceNumber = invoiceNumber;
    this.invoiceNetAmount = invoiceNetAmount;
    this.invoiceTaxAmount = invoiceTaxAmount;
    this.invoiceGrossAmount = invoiceGrossAmount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Participant getCompany() {
    return company;
  }

  public void setCompany(Participant company) {
    this.company = company;
  }

  public LocalDate getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public double getInvoiceNetAmount() {
    return invoiceNetAmount;
  }

  public void setInvoiceNetAmount(double invoiceNetAmount) {
    this.invoiceNetAmount = invoiceNetAmount;
  }

  public double getInvoiceTaxAmount() {
    return invoiceTaxAmount;
  }

  public void setInvoiceTaxAmount(double invoiceTaxAmount) {
    this.invoiceTaxAmount = invoiceTaxAmount;
  }

  public double getInvoiceGrossAmount() {
    return invoiceGrossAmount;
  }

  public void setInvoiceGrossAmount(double invoiceGrossAmount) {
    this.invoiceGrossAmount = invoiceGrossAmount;
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
    List<LocalDate> dates = subscription.getUserModificationDatesBetween(feeItem.getBeginDate(), feeItem.getEndDate());
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

  public List<FeeItem> getFeeItems() {
    List<FeeItem> result = new LinkedList<>();
    for (InvoiceByUserAndPhoneNumber part : invoicePart) {
      result.addAll(part.getFees());
    }
    return result;
  }

  public boolean canDelete() {
    return !acceptedByUsers && !acceptedByCompany;
  }

  // TODO konzisztenciát ellenőrző függvényt elkészíteni
  public boolean isConsistent() {
    return true;
  }

  public void setAcceptedByCompany() {
    acceptedByCompany = true;
    int partOfCompany = 0;
    for (InvoiceByUserAndPhoneNumber part : invoicePart) {
      part.removeRevisionNote();
      part.setAcceptedByCompany(true);
      if (part.getUser() == null) {
        part.setAcceptedByUser(true);
        partOfCompany++;
      }
    }
    if (partOfCompany == invoicePart.size()) {
      acceptedByUsers = true;
    }
  }

  public boolean hasAnyRevisionNote() {
    for (InvoiceByUserAndPhoneNumber part : invoicePart) {
      if (part.hasRevisionNote() || part.hasAnyRevisionNoteOfFees()) {
        return true;
      }
    }
    return false;
  }

//  public void setCategoryOfFees(DescriptionCategoryCoupler dcc) {
//    for (FeeItem feeItem : getFeeItems()) {
//      feeItem.setCategory(dcc.getCategoryByDescription(feeItem.getDescription()));
//    }
//  }

  public String getPeriod() {
    return Utility.getPeriod(beginDate, endDate);
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((invoiceNumber == null) ? 0 : invoiceNumber.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Invoice other = (Invoice) obj;
    if (invoiceNumber == null) {
      if (other.invoiceNumber != null)
        return false;
    } else if (!invoiceNumber.equals(other.invoiceNumber))
      return false;
    return true;
  }
  

}
