package com.flotta.invoice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flotta.utility.Utility;

@Entity
@Table(name = "fee_items")
public class FeeItem {

  @Id
  @GeneratedValue
  long id;

  @ManyToOne
  private InvoiceByUserAndPhoneNumber invoiceByUserAndPhoneNumber;

  private String subscription;
  
  private long userId;

  private String description;

  private LocalDate beginDate;

  private LocalDate endDate;

  private double netAmount;

  private double taxAmount;

  private double taxPercentage;
  
  private double userGrossAmount;
  
  private double companyGrossAmount;
  
  private double totalGrossAmount;
  
  @OneToOne
  private Category category;
  
  private String revisionNote;
  
  public FeeItem() {
  }

  public FeeItem(String subscription, 
                 String description, 
                 LocalDate beginDate, 
                 LocalDate endDate, 
                 double netAmount, 
                 double taxAmount, 
                 double taxPercentage, 
                 double totalGrossAmount) {
    this.subscription = subscription;
    this.description = description;
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.netAmount = netAmount;
    this.taxAmount = taxAmount;
    this.taxPercentage = taxPercentage;
    this.companyGrossAmount = totalGrossAmount;
    this.totalGrossAmount = totalGrossAmount;
  }

  public FeeItem(FeeItem feeItem) {
    this.invoiceByUserAndPhoneNumber = feeItem.invoiceByUserAndPhoneNumber;
    this.subscription = feeItem.subscription;
    this.description = feeItem.description;
    this.beginDate = feeItem.beginDate;
    this.endDate = feeItem.endDate;
    this.netAmount = feeItem.netAmount;
    this.taxAmount = feeItem.taxAmount;
    this.taxPercentage = feeItem.taxPercentage;
    this.userGrossAmount = feeItem.userGrossAmount;
    this.companyGrossAmount = feeItem.companyGrossAmount;
    this.totalGrossAmount = feeItem.totalGrossAmount;
    this.revisionNote = feeItem.revisionNote;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public InvoiceByUserAndPhoneNumber getInvoiceByUserAndPhoneNumber() {
    return invoiceByUserAndPhoneNumber;
  }

  public void setInvoiceByUserAndPhoneNumber(InvoiceByUserAndPhoneNumber invoiceByUserAndPhoneNumber) {
    this.invoiceByUserAndPhoneNumber = invoiceByUserAndPhoneNumber;
  }

  public String getSubscription() {
    return subscription;
  }

  public void setSubscription(String subscription) {
    this.subscription = subscription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public double getNetAmount() {
    return netAmount;
  }

  public void setNetAmount(double netAmount) {
    this.netAmount = netAmount;
  }

  public double getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(double taxAmount) {
    this.taxAmount = taxAmount;
  }

  public double getTaxPercentage() {
    return taxPercentage;
  }

  public void setTaxPercentage(double taxPercentage) {
    this.taxPercentage = taxPercentage;
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

  public double getTotalGrossAmount() {
    return totalGrossAmount;
  }

  public void setTotalGrossAmount(double totalGrossAmount) {
    this.totalGrossAmount = totalGrossAmount;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
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

  public List<FeeItem> splitBeforeDate(List<LocalDate> dates) {
    List<FeeItem> result = new LinkedList<>();
    Collections.sort(dates);
    LocalDate b = beginDate;
    for (LocalDate date : dates) {
      if (date.isEqual(beginDate) || date.isBefore(beginDate) || date.isAfter(endDate)) {
        // nothing
      } else {
        FeeItem fee = getPartOfFeeItem(b, date);
        
        result.add(fee);
        b = date;
      }
    }
    FeeItem last = getPartOfFeeItem(b, endDate.plusDays(1));
    last.setId(this.id);
    result.add(last);
    return result;
  }

  // LocalDate b is inclusive
  // LocalDate e is exclusive
  private FeeItem getPartOfFeeItem(LocalDate b, LocalDate e) {
    long all = beginDate.until(endDate, ChronoUnit.DAYS) + 1;
    long part = b.until(e, ChronoUnit.DAYS);
    FeeItem result = new FeeItem(this);
    result.setId(0);
    result.setBeginDate(b);
    result.setEndDate(e.minusDays(1));
    result.setNetAmount(Utility.round(netAmount * part / all, 2));
    result.setTaxAmount(Utility.round(taxAmount * part / all, 2));
    result.setUserGrossAmount(Utility.round(userGrossAmount * part / all, 2));
    result.setCompanyGrossAmount(Utility.round(companyGrossAmount * part / all, 2));
    result.setTotalGrossAmount(Utility.round(totalGrossAmount * part / all, 2));
    return result;
  }
  
  public String getPeriod() {
    if(beginDate == null) {
      return "";
    } else {
      return beginDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " - " + endDate.format(DateTimeFormatter.ofPattern("MM.dd"));
    }
  }

  public boolean hasRevisionNote() {
    return revisionNote != null;
  }
  
}
