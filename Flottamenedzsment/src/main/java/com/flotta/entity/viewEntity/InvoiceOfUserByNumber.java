package com.flotta.entity.viewEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import com.flotta.invoice.FeeItem;

public class InvoiceOfUserByNumber {
  private String number;
  
  private String invoiceNumber;
  
  private LocalDate begin;
  
  private LocalDate end;
  
  private double grossAmount;
  
  private List<FeeItem> fees = new LinkedList<>();
  
  public InvoiceOfUserByNumber() {}
  
  public InvoiceOfUserByNumber(String number, String invoiceNumber) {
    this.number = number;
    this.invoiceNumber = invoiceNumber;
  }
  
  public void addFeeItem(FeeItem item) {
    if(item.getSubscription().equals(this.number)
//        && item.getInvoice().getInvoiceNumber().equals(this.invoiceNumber)
        ) {
      if(begin == null) {
        begin = item.getBeginDate();
        end = item.getEndDate();
      } else {
        if(item.getBeginDate().isBefore(begin)) {
          begin = item.getBeginDate();
        }
        if(item.getEndDate().isAfter(end)) {
          end = item.getEndDate();
        }
      }
      grossAmount += item.getUserGrossAmount();
      fees.add(item);
    }
  }
  
  public String getPeriod() {
    if(begin == null) {
      return "";
    } else {
      return begin.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " - " + end.format(DateTimeFormatter.ofPattern("MM.dd"));
    }
  }
  
  public String getNumber() {
    return this.number;
  }
  
  public double getGrossAmount() {
    return this.grossAmount;
  }
  
  public List<FeeItem> getFees() {
    return fees;
  }
  
  public LocalDate getBegin() {
    return begin;
  }
  
  public LocalDate getEnd() {
    return end;
  }
}
