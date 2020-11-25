package com.flotta.model.invoice;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BasicInvoice extends BasicAccountElement {
  
  protected String invoiceNumber;

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }
}
