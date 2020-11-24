package com.flotta.entity.invoice;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.utility.Utility;

@Entity
@Table(name = "raw_invoices")
public class RawInvoice {

  @Id
  @GeneratedValue
  private long id;
  
  private String customerName;
  private String customerAddress;
  
  private String companyName;
  private String companyAddress;
  
  private LocalDate beginDate;
  
  private LocalDate endDate;
  
  private String invoiceNumber;
  
  private double invoiceNetAmount;
  
  private double invoiceTaxAmount;
  
  private double invoiceGrossAmount;
  
  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
  private List<RawFeeItem> feeItems = new LinkedList<>();
  
  @ElementCollection
  private Set<String> problems = new HashSet<>();
  
  public RawInvoice() {}
  
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerAddress() {
    return customerAddress;
  }

  public void setCustomerAddress(String customerAddress) {
    this.customerAddress = customerAddress;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getCompanyAddress() {
    return companyAddress;
  }

  public void setCompanyAddress(String companyAddress) {
    this.companyAddress = companyAddress;
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
  
  public List<RawFeeItem> getFeeItems() {
    return feeItems;
  }

  public void setFeeItems(List<RawFeeItem> feeItems) {
    this.feeItems = feeItems;
  }

  public void addFeeItem(RawFeeItem feeItem) {
    feeItem.setInvoice(this);
    feeItems.add(feeItem);
  }

  public Set<String> getProblems() {
    return problems;
  }

  public void setProblems(Set<String> problems) {
    this.problems = problems;
  }

  
  public void addProblem(String problem) {
    problems.add(problem);
  }
  
  public void clearProblem() {
    problems.clear();
  }

  public boolean hasProblem() {
    return !problems.isEmpty();
  }

  public List<String> getAllDescription() {
    Set<String> descriptions = new HashSet<>();
    for (RawFeeItem feeItem : feeItems) {
      descriptions.add(feeItem.getDescription());
    }
    return new LinkedList<>(descriptions);
  }
  
  public String getPeriod() {
    return Utility.getPeriod(beginDate, endDate);
  }
}
