package com.flotta.repository.invoice;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.invoice.RawInvoice;

public interface RawInvoiceRepository extends CrudRepository<RawInvoice, Long> {
  
  List<RawInvoice> findAll();

  RawInvoice findByInvoiceNumber(String invoiceNumber);

  void deleteByInvoiceNumber(String invoiceNumber);

}
