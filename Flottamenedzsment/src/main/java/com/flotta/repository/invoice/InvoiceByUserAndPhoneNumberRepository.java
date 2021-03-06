package com.flotta.repository.invoice;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.invoice.InvoiceByUserAndPhoneNumber;
import com.flotta.entity.record.User;

public interface InvoiceByUserAndPhoneNumberRepository extends CrudRepository<InvoiceByUserAndPhoneNumber, Long> {
  
  //Felhasználóhoz tartózó összes felhasználó elfogadására váró
  List<InvoiceByUserAndPhoneNumber> findAllByUserAndAcceptedByCompanyTrueAndAcceptedByUserFalse(User user);

  //Konkrét id-vel, ellenőrizve hogy a felhasználóhoz tartozik
  Optional<InvoiceByUserAndPhoneNumber> findByIdAndUser(Long id, User user);

  //Felhasználóhoz tartozó összes lezárt (mindkét oldalról elfogadott)
  List<InvoiceByUserAndPhoneNumber> findAllByUserAndAcceptedByCompanyTrueAndAcceptedByUserTrue(User user);

  //Konkrét id-vel, ellenőrizve hogy a felhasználóhoz tartozik és lezárt (mindkét oldalról elfogadott)
  InvoiceByUserAndPhoneNumber findByIdAndUserAndAcceptedByCompanyTrueAndAcceptedByUserTrue(long id, User user);
  
  List<InvoiceByUserAndPhoneNumber> findAllByUser(User user);
  
  List<InvoiceByUserAndPhoneNumber> findAllByUserOrderByAcceptedByUserAscBeginDateAsc(User user);

  List<InvoiceByUserAndPhoneNumber> findAllByUserAndAcceptedByCompanyTrueOrderByAcceptedByUserAscBeginDateAsc(User user);
}