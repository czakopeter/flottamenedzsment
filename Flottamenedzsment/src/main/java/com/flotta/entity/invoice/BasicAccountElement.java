package com.flotta.entity.invoice;

import java.time.LocalDate;

import javax.persistence.MappedSuperclass;

import com.flotta.entity.BasicEntity;
import com.flotta.utility.Utility;

@MappedSuperclass
public abstract class BasicAccountElement extends BasicEntity {

  protected LocalDate beginDate;

  protected LocalDate endDate;

  protected double netAmount;

  protected double taxAmount;

  protected double grossAmount;

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

  public double getGrossAmount() {
    return grossAmount;
  }

  public void setGrossAmount(double grossAmount) {
    this.grossAmount = grossAmount;
  }
  
  public String getPeriod() {
    return Utility.getPeriod(beginDate, endDate);
  }
}
