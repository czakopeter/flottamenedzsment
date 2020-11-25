package com.flotta.entity.invoice;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "raw_fee_items")
public class RawFeeItem extends BasicFeeItem {

  @ManyToOne
  private RawInvoice invoice;

  public RawFeeItem() {
  }

  public RawFeeItem(String subscription, 
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
    this.grossAmount = grossAmount;
  }

  public RawInvoice getInvoice() {
    return invoice;
  }

  public void setInvoice(RawInvoice invoice) {
    this.invoice = invoice;
  }

}
