package com.flotta.entity.invoice;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flotta.utility.Utility;

@Entity
@Table(name = "fee_items")
public class FeeItem extends BasicFeeItem {

  @ManyToOne
  private InvoiceByUserAndPhoneNumber invoiceByUserAndPhoneNumber;

  private long userId;

  private double userGrossAmount;
  
  private double companyGrossAmount;
  
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
                 double grossAmount) {
    this.subscription = subscription;
    this.description = description;
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.netAmount = netAmount;
    this.taxAmount = taxAmount;
    this.companyGrossAmount = grossAmount;
    this.grossAmount = grossAmount;
  }

  public FeeItem(FeeItem feeItem) {
    this.invoiceByUserAndPhoneNumber = feeItem.invoiceByUserAndPhoneNumber;
    this.subscription = feeItem.subscription;
    this.description = feeItem.description;
    this.beginDate = feeItem.beginDate;
    this.endDate = feeItem.endDate;
    this.netAmount = feeItem.netAmount;
    this.taxAmount = feeItem.taxAmount;
    this.grossAmount = feeItem.grossAmount;
    this.userGrossAmount = feeItem.userGrossAmount;
    this.companyGrossAmount = feeItem.companyGrossAmount;
    this.revisionNote = feeItem.revisionNote;
    this.category = feeItem.category;
  }

  public InvoiceByUserAndPhoneNumber getInvoiceByUserAndPhoneNumber() {
    return invoiceByUserAndPhoneNumber;
  }

  public void setInvoiceByUserAndPhoneNumber(InvoiceByUserAndPhoneNumber invoiceByUserAndPhoneNumber) {
    this.invoiceByUserAndPhoneNumber = invoiceByUserAndPhoneNumber;
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
    LocalDate begin = beginDate;
    for (LocalDate date : dates) {
      if (date.isEqual(beginDate) || date.isBefore(beginDate) || date.isAfter(endDate)) {
        // nothing
      } else {
        FeeItem fee = getPartOfFeeItem(begin, date);
        
        result.add(fee);
        begin = date;
      }
    }
    FeeItem last = getPartOfFeeItem(begin, endDate.plusDays(1));
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
    result.setCategory(category);
    result.setNetAmount(Utility.round(netAmount * part / all, 2));
    result.setTaxAmount(Utility.round(taxAmount * part / all, 2));
    result.setUserGrossAmount(Utility.round(userGrossAmount * part / all, 2));
    result.setCompanyGrossAmount(Utility.round(companyGrossAmount * part / all, 2));
    result.setGrossAmount(Utility.round(grossAmount * part / all, 2));
    return result;
  }
  
  public void setChargeRatioByUserPercentage(int userRatio) {
    userGrossAmount = Utility.round(grossAmount * userRatio / 100, 2);
    companyGrossAmount = Utility.round(grossAmount * (100 - userRatio) / 100, 2);
  }
  
  public boolean hasRevisionNote() {
    return revisionNote != null;
  }

}
