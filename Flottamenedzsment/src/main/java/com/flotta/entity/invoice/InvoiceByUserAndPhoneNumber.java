package com.flotta.entity.invoice;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.entity.record.Subscription;
import com.flotta.entity.record.User;
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
  private List<FeeItem> fees = new LinkedList<>();

  private double userGrossAmount;
  
  private double companyGrossAmount;
  
  private boolean acceptedByUser;
  
  private boolean acceptedByCompany;
  
  private String revisionNote;
  
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

  public List<FeeItem> getFees() {
    return fees;
  }

  public void setFees(List<FeeItem> fees) {
    this.fees = fees;
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

  public String getRevisionNote() {
    return revisionNote;
  }

  public void setRevisionNote(String revisionNote) {
    if(revisionNote == null || revisionNote.isEmpty()) {
      this.revisionNote = null;
    } else {
      this.revisionNote = revisionNote;
    }
  }

  public void addFeeItem(FeeItem item) {
    item.setInvoiceByUserAndPhoneNumber(this);
    amountUpdate(item);
    dateUpdate(item);
    fees.add(item);
  }
  
  private void amountUpdate(FeeItem item) {
    this.userGrossAmount += item.getUserGrossAmount();
    this.companyGrossAmount += item.getCompanyGrossAmount();
    this.netAmount += item.getNetAmount();
    this.taxAmount += item.getTaxAmount();
    this.grossAmount += item.getGrossAmount();
  }
  
  private void dateUpdate(FeeItem item) {
    if(beginDate == null) {
      beginDate = item.getBeginDate();
      endDate = item.getEndDate();
    } else {
      if(item.getBeginDate().isBefore(beginDate)) {
        beginDate = item.getBeginDate();
      }
      if(item.getEndDate().isAfter(endDate)) {
        endDate = item.getEndDate();
      }
    }
  }
  
  public boolean isClosed() {
    return acceptedByCompany && acceptedByUser;
  }

  public void updateAmountsByFeeItems() {
    this.netAmount = 0;
    this.taxAmount = 0;
    this.userGrossAmount = 0;
    this.companyGrossAmount = 0.0;
    this.grossAmount = 0;
    for(FeeItem item : fees) {
      amountUpdate(item);
    }
  }
  
  public String getPeriod() {
    return Utility.getPeriod(beginDate, endDate);
  }

  public void setRevisionNoteOfFeeItem(long id, String note) {
    for(FeeItem fee : fees) {
      if(fee.getId() == id) {
        fee.setRevisionNote(note);
      }
    }
  }

  public boolean hasRevisionNote() {
    return revisionNote != null;
  }
  
  public boolean hasAnyRevisionNote() {
    return this.hasRevisionNote() || this.hasAnyRevisionNoteOfFees();
  }
  
  public boolean hasAnyRevisionNoteOfFees() {
    for(FeeItem fee : fees) {
      if(fee.hasRevisionNote()) {
        return true;
      }
    }
    return false;
  }

  public void removeRevisionNote() {
    revisionNote = null;
    for(FeeItem feeItem : fees) {
      feeItem.setRevisionNote(null);
    }
  }
  
  public Set<String> getAllDescription() {
    Set<String> descriptions = new HashSet<>();
    for(FeeItem feeItem : fees) {
      descriptions.add(feeItem.getDescription());
    }
    return descriptions;
  }
}
