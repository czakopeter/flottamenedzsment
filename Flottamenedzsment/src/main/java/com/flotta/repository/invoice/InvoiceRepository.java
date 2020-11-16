package com.flotta.repository.invoice;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.invoice.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
  
  List<Invoice> findAll();

  Invoice findByInvoiceNumber(String invoiceNumber);

}
