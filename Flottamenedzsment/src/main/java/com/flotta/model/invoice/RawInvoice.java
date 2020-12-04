package com.flotta.model.invoice;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "raw_invoices")
public class RawInvoice extends BasicInvoice {

  public static final Comparator<RawInvoice> BY_INVOICE_NUMBER_ADN_ACCEPT_BY_COMPANY_AMD_DATE = 
      Comparator.comparing(RawInvoice::getBeginDate).thenComparing(RawInvoice::getInvoiceNumber);
  
  private String companyName;
  private String companyAddress;
  
  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
  private List<RawFeeItem> feeItems = new LinkedList<>();
  
  @ElementCollection
  private Set<String> problems = new HashSet<>();
  
  public RawInvoice() {}
  
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
  
}
