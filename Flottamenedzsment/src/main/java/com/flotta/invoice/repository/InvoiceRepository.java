package com.flotta.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.invoice.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
  
  List<Invoice> findAll();

  Invoice findByInvoiceNumber(String invoiceNumber);

}
