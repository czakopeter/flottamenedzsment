package com.flotta.invoice;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flotta.entity.BasicEntity;
import com.flotta.entity.Subscription;
import com.flotta.entity.User;

@Entity
@Table(name = "invoice_parts")
public class InvoiceByUserAndPhoneNumber extends BasicEntity {

  @ManyToOne
  @JsonIgnore
  private Invoice invoice;

  @ManyToOne
  private Subscription subscription;
  
  @ManyToOne
  private User user;
  
  @OneToMany(mappedBy = "invoiceByUserAndPhoneNumber", cascade = CascadeType.ALL)
  private List<FeeItem> fees = new LinkedList<>();

  private LocalDate beginDate;

  private LocalDate endDate;

  private double totalNetAmount;

  private double totalTaxAmount;

  private double userGrossAmount;
  
  private double totalGrossAmount;
  
  private boolean closed;
  
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

  public double getTotalNetAmount() {
    return totalNetAmount;
  }

  public void setTotalNetAmount(double totalNetAmount) {
    this.totalNetAmount = totalNetAmount;
  }

  public double getTotalTaxAmount() {
    return totalTaxAmount;
  }

  public void setTotalTaxAmount(double totalTaxAmount) {
    this.totalTaxAmount = totalTaxAmount;
  }

  public double getUserGrossAmount() {
    return userGrossAmount;
  }

  public void setUserGrossAmount(double userGrossAmount) {
    this.userGrossAmount = userGrossAmount;
  }

  public double getTotalGrossAmount() {
    return totalGrossAmount;
  }

  public void setTotalGrossAmount(double totalGrossAmount) {
    this.totalGrossAmount = totalGrossAmount;
  }

  public boolean isClosed() {
    return closed;
  }

  public void setClosed(boolean closed) {
    this.closed = closed;
  }

  public String getRevisionNote() {
    return revisionNote;
  }

  public void setRevisionNote(String revisionNote) {
    this.revisionNote = revisionNote;
  }
  
  

  public void addFeeItem(FeeItem item) {
    item.setInvoiceByUserAndPhoneNumber(this);
    amountUpdate(item);
    dateUpdate(item);
    fees.add(item);
  }
  
  private void amountUpdate(FeeItem item) {
    this.totalNetAmount += item.getNetAmount();
    this.totalTaxAmount += item.getTaxAmount();
    this.totalGrossAmount += item.getTotalGrossAmount();
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
  

//  public List<InvoiceByUserAndPhoneNumber> splitBeforeDate(List<LocalDate> dates) {
//    List<InvoiceByUserAndPhoneNumber> result = new LinkedList<>();
//    Collections.sort(dates);
//    LocalDate b = beginDate;
//    for (LocalDate date : dates) {
//      if (date.isEqual(beginDate) || date.isBefore(beginDate) || date.isAfter(endDate)) {
//        // nothing
//      } else {
//        InvoiceByUserAndPhoneNumber fee = getPartOfFeeItem(b, date);
//        
//        result.add(fee);
//        b = date;
//      }
//    }
//    InvoiceByUserAndPhoneNumber last = getPartOfFeeItem(b, endDate.plusDays(1));
//    last.setId(this.id);
//    result.add(last);
//    return result;
//  }

  // LocalDate b is inclusive
  // LocalDate e is exclusive
//  private InvoiceByUserAndPhoneNumber getPartOfFeeItem(LocalDate b, LocalDate e) {
//    long all = beginDate.until(endDate, ChronoUnit.DAYS) + 1;
//    long part = b.until(e, ChronoUnit.DAYS);
//    InvoiceByUserAndPhoneNumber result = new InvoiceByUserAndPhoneNumber(this);
//    result.setId(0);
//    result.setBegin(b);
//    result.setEnd(e.minusDays(1));
//    result.setNetAmount(round(netAmount * part / all, 2));
//    result.setTaxAmount(round(taxAmount * part / all, 2));
//    result.setUserGross(round(userGross * part / all, 2));
//    result.setCompGross(round(companyGross * part / all, 2));
//    result.setValidByUser(false);
//    result.setValidByCompany(false);
//    return result;
//  }
  
  
}
