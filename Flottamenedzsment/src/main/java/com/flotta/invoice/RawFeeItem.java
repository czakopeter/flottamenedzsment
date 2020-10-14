package com.flotta.invoice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "raw_fee_items")
public class RawFeeItem {

  @Id
  @GeneratedValue
  long id;

  @ManyToOne
  private RawInvoice invoice;

  private String subscription;
  
  private String description;

  private LocalDate beginDate;

  private LocalDate endDate;

  private double netAmount;

  private double taxAmount;

  private double taxPercentage;
  
  private double grossAmount;
  
  public RawFeeItem() {
  }

  public RawFeeItem(String subscription, 
                 String description, 
                 LocalDate beginDate, 
                 LocalDate endDate, 
                 double netAmount, 
                 double taxAmount, 
                 double taxPercentage, 
                 double grossAmount) {
    this.subscription = subscription;
    this.description = description;
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.netAmount = netAmount;
    this.taxAmount = taxAmount;
    this.taxPercentage = taxPercentage;
    this.grossAmount = grossAmount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public RawInvoice getInvoice() {
    return invoice;
  }

  public void setInvoice(RawInvoice invoice) {
    this.invoice = invoice;
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
  
  public double getGrossAmount() {
    return grossAmount;
  }

  public void setGrossAmount(double grossAmount) {
    this.grossAmount = grossAmount;
  }

  public String getPeriod() {
    if(beginDate == null) {
      return "";
    } else {
      return beginDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " - " + endDate.format(DateTimeFormatter.ofPattern("MM.dd"));
    }
  }

}
