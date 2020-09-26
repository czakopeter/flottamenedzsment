package com.flotta.invoice;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.entity.Subscription;
import com.flotta.entity.User;

import net.bytebuddy.utility.privilege.GetSystemPropertyAction;

@Entity
@Table(name = "invoices")
public class Invoice {

  @Id
  @GeneratedValue
  private long id;
  
  @Lob
  private String xmlString;
  
  //TOTO createInvoiceHead 
  
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
  
  public Invoice() {}
  
  public Invoice(String xmlString, LocalDate fromDate, LocalDate endDate, String invoiceNumber, Double invoiceNetAmount, Double invoiceTaxAmount, double invoiceGrossAmount) {
    this.xmlString = xmlString;
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

  public String getXmlString() {
    return xmlString;
  }

  public void setXmlString(String xmlString) {
    this.xmlString = xmlString;
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

  //TODO átalakít
  public void addFeeItem(Subscription subscription, FeeItem feeItem) {
    if(subscription == null) {
      for(InvoiceByUserAndPhoneNumber part : invoicePart) {
        if(part.getSubscription() == null) {
          part.addFeeItem(feeItem);
          return;
        }
      }
      InvoiceByUserAndPhoneNumber part = new InvoiceByUserAndPhoneNumber(this, null, null);
      part.addFeeItem(feeItem);
      invoicePart.add(part);
      return;
    } else {
      List<FeeItem> fees = splitByUser(feeItem, subscription);
      for(FeeItem fee : fees) {
        boolean hasPart = false;
        for(InvoiceByUserAndPhoneNumber part : invoicePart) {
          if((fee.getUserId() == 0 && part.getUser() == null) || 
             (part.getUser() != null && fee.getUserId() == part.getUser().getId() && part.getSubscription().equals(subscription))) {
            part.addFeeItem(fee);
            hasPart = true;
            break;
          }
        }
        if(!hasPart) {
          InvoiceByUserAndPhoneNumber part = new InvoiceByUserAndPhoneNumber(this, subscription, subscription.getUserByDate(fee.getBeginDate()));
          part.addFeeItem(fee);
          invoicePart.add(part);
        }
      }
    }
  }
  
  private List<FeeItem> splitByUser(FeeItem feeItem, Subscription subscription) {
    List<LocalDate> dates = subscription.getUserModificationDatesBetween(feeItem.getBeginDate(), feeItem.getEndDate());
    List<FeeItem> fees = feeItem.splitBeforeDate(dates);
    for(FeeItem fee : fees) {
      User user = subscription.getUserByDate(fee.getBeginDate());
      fee.setUserId(user != null ? user.getId() : 0);
    }
    return fees;
  }
  
  public List<FeeItem> getFeeItems() {
    List<FeeItem> result = new LinkedList<>();
    for(InvoiceByUserAndPhoneNumber part : invoicePart) {
      result.addAll(part.getFees());
    }
    return result;
  }
  
  public boolean hasProblem() {
    for(InvoiceByUserAndPhoneNumber part : invoicePart) {
      if(part.getSubscription() == null) {
        return true;
      }
    }
    return false;
  }
  
  public String getProblem() {
    StringBuilder sb = new StringBuilder();
    Set<String> phoneNumbers = new HashSet<>();
    for(InvoiceByUserAndPhoneNumber part : invoicePart) {
      if(part.getSubscription() == null) {
        for(FeeItem fee : part.getFees()) {
           phoneNumbers.add(fee.getSubscription());
        }
      }
    }
    if(phoneNumbers.size() != 0) {
      sb.append("Unknown phone numbers:");
      for(String number : phoneNumbers) {
        sb.append("\n - " + number);
      }
    }
    
    return sb.toString();
  }

  public boolean canDelete() {
    return !acceptedByUsers && !acceptedByCompany;
  }

  //TODO konzisztenciát ellenőrző függvényt elkészíteni
  public boolean isConsistent() {
    return true;
  }

  public void setAcceptedByCompany() {
    this.acceptedByCompany = true;
    if(hasAnyRevisionNote()) {
      for(InvoiceByUserAndPhoneNumber part : invoicePart) {
        if(part.hasRevisionNote()) {
          part.setRevisionNote("");
          part.setAcceptedByCompany(true);
        }
        for(FeeItem feeItem : part.getFees()) {
          if(feeItem.hasRevisionNote()) {
            feeItem.setRevisionNote("");
          }
        }
      }
    } else {
      for(InvoiceByUserAndPhoneNumber part : invoicePart) {
        part.setAcceptedByCompany(true);
      }
    }
  }
  
  public boolean hasAnyRevisionNote() {
    for(InvoiceByUserAndPhoneNumber part : invoicePart) {
      if(part.hasRevisionNote() || part.hasAnyRevisionNoteOfFees()) {
        return true;
      }
    }
    return false;
  }
  
}
