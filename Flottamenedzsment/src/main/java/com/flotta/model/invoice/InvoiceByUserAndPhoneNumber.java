package com.flotta.model.invoice;

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
import com.flotta.utility.Utility;

@Entity
@Table(name = "invoice_parts")
public class InvoiceByUserAndPhoneNumber extends BasicAccountElement {

  @ManyToOne
  private Invoice invoice;

  @ManyToOne
  private Subscription subscription;
  
  @ManyToOne
  private User user;
  
  @OneToMany(mappedBy = "invoiceByUserAndPhoneNumber", cascade = CascadeType.ALL)
  private List<FeeItem> feeItems = new LinkedList<>();

  private double userGrossAmount;
  
  private double companyGrossAmount;
  
  private boolean acceptedByUser;
  
  private boolean acceptedByCompany;
  
  private String reviewNote;
  
  public InvoiceByUserAndPhoneNumber() {
  }

  public InvoiceByUserAndPhoneNumber(Invoice invoice, Subscription subscription, User user) {
    this.invoice = invoice;
    this.subscription = subscription;
    this.user = user;
  }

  public Invoice getInvoice() {
    return invoice;
  }

  public void setInvoice(Invoice invoice) {
    this.invoice = invoice;
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public void setSubscription(Subscription subscription) {
    this.subscription = subscription;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<FeeItem> getFeeItems() {
    return feeItems;
  }

  public void setFeeItems(List<FeeItem> feeItems) {
    this.feeItems = feeItems;
  }


  public double getUserGrossAmount() {
    return userGrossAmount;
  }

  public void setUserGrossAmount(double userGrossAmount) {
    this.userGrossAmount = userGrossAmount;
  }
  
  public double getCompanyGrossAmount() {
    return companyGrossAmount;
  }

  public void setCompanyGrossAmount(double companyGrossAmount) {
    this.companyGrossAmount = companyGrossAmount;
  }

  public boolean isAcceptedByUser() {
    return acceptedByUser;
  }

  public void setAcceptedByUser(boolean acceptedByUser) {
    this.acceptedByUser = acceptedByUser;
  }

  public boolean isAcceptedByCompany() {
    return acceptedByCompany;
  }

  public void setAcceptedByCompany(boolean acceptedByCompany) {
    this.acceptedByCompany = acceptedByCompany;
  }

  public String getReviewNote() {
    return reviewNote;
  }

  public void setReviewNote(String reviewNote) {
    if(reviewNote == null || reviewNote.isEmpty()) {
      this.reviewNote = null;
    } else {
      this.reviewNote = reviewNote;
    }
  }

  public void addFeeItem(FeeItem feeItem) {
    feeItem.setInvoiceByUserAndPhoneNumber(this);
    amountUpdate(feeItem);
    dateUpdate(feeItem);
    feeItems.add(feeItem);
  }
  
  private void amountUpdate(FeeItem feeItem) {
    this.userGrossAmount += feeItem.getUserGrossAmount();
    this.companyGrossAmount += feeItem.getCompanyGrossAmount();
    this.netAmount += feeItem.getNetAmount();
    this.taxAmount += feeItem.getTaxAmount();
    this.grossAmount += feeItem.getGrossAmount();
  }
  
  private void dateUpdate(FeeItem feeItem) {
    if(beginDate == null) {
      beginDate = feeItem.getBeginDate();
      endDate = feeItem.getEndDate();
    } else {
      if(feeItem.getBeginDate().isBefore(beginDate)) {
        beginDate = feeItem.getBeginDate();
      }
      if(feeItem.getEndDate().isAfter(endDate)) {
        endDate = feeItem.getEndDate();
      }
    }
  }
  
  public boolean isClosed() {
    return acceptedByCompany && acceptedByUser;
  }

  public void setAmountsByFeeItems() {
    this.netAmount = 0;
    this.taxAmount = 0;
    this.userGrossAmount = 0;
    this.companyGrossAmount = 0.0;
    this.grossAmount = 0;
    for(FeeItem feeItem : feeItems) {
      amountUpdate(feeItem);
    }
  }
  
  public void setReviewNoteOfFeeItem(long id, String note) {
    for(FeeItem feeItem : feeItems) {
      if(feeItem.getId() == id) {
        feeItem.setReviewNote(note);
      }
    }
  }

  public boolean hasReviewNote() {
    return reviewNote != null;
  }
  
  public boolean hasAnyReviewNote() {
    return this.hasReviewNote() || this.hasAnyReviewNoteOfFeeItems();
  }
  
  public boolean hasAnyReviewNoteOfFeeItems() {
    for(FeeItem feeItem : feeItems) {
      if(feeItem.hasReviewNote()) {
        return true;
      }
    }
    return false;
  }

  public void removeAllReviewNote() {
    reviewNote = null;
    for(FeeItem feeItem : feeItems) {
      feeItem.setReviewNote(null);
    }
  }
  
  public Set<String> getAllDescription() {
    Set<String> descriptions = new HashSet<>();
    for(FeeItem feeItem : feeItems) {
      descriptions.add(feeItem.getDescription());
    }
    return descriptions;
  }
}
