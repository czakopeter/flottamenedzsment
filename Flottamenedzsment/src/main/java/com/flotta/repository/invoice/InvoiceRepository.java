package com.flotta.repository.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.invoice.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
  
  List<Invoice> findAll();

  Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

}
