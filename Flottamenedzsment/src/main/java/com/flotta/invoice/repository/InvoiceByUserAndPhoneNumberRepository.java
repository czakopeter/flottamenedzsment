package com.flotta.invoice.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.Subscription;
import com.flotta.entity.User;
import com.flotta.invoice.Invoice;
import com.flotta.invoice.InvoiceByUserAndPhoneNumber;

public interface InvoiceByUserAndPhoneNumberRepository extends CrudRepository<InvoiceByUserAndPhoneNumber, Long> {

  InvoiceByUserAndPhoneNumber findByUserAndInvoiceAndSubscriptionAndAcceptedByCompanyTrue(User user, Invoice invoice, Subscription subscription);

  List<InvoiceByUserAndPhoneNumber> findAllByUserAndAcceptedByCompanyTrueAndAcceptedByUserFalse(User user);

  InvoiceByUserAndPhoneNumber findByUserAndInvoiceAndSubscriptionAndAcceptedByCompanyTrueAndAcceptedByUserFalse(User user, Invoice invoiceNumber, Subscription subscription);
  
  Optional<InvoiceByUserAndPhoneNumber> findByIdAndUser(Long id, User user);

  List<InvoiceByUserAndPhoneNumber> findAllByUserAndAcceptedByCompanyTrueAndAcceptedByUserTrue(User user);

  InvoiceByUserAndPhoneNumber findByIdAndUserAndAcceptedByCompanyTrueAndAcceptedByUserTrue(long id, User user);
}
