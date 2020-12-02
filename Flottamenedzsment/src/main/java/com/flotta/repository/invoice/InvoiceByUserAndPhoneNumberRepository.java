package com.flotta.repository.invoice;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.model.registry.User;

public interface InvoiceByUserAndPhoneNumberRepository extends CrudRepository<InvoiceByUserAndPhoneNumber, Long> {
  
  //Konkrét id-vel, ellenőrizve hogy a felhasználóhoz tartozik
  Optional<InvoiceByUserAndPhoneNumber> findByIdAndUser(Long id, User user);

  //Visszaadja a felhasználó cég által már elfogadott számláját, lista elején a még nem elfogadottak, azon belöl dátum szerint rendezi
  List<InvoiceByUserAndPhoneNumber> findAllByUserAndAcceptedByCompanyTrueOrderByAcceptedByUserAscBeginDateAsc(User user);

  void save(List<InvoiceByUserAndPhoneNumber> invoices);

}