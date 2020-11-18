package com.flotta.repository.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.invoice.RawInvoice;

public interface RawInvoiceRepository extends CrudRepository<RawInvoice, Long> {
  
  List<RawInvoice> findAll();

  Optional<RawInvoice> findByInvoiceNumber(String invoiceNumber);

}
