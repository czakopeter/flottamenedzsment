package com.flotta.repository.invoice;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.invoice.GroupedFeeItems;
import com.flotta.model.registry.User;

public interface GroupedFeeItemsRepository extends CrudRepository<GroupedFeeItems, Long> {
  
  //Konkrét id-vel, ellenőrizve hogy a felhasználóhoz tartozik
  Optional<GroupedFeeItems> findByIdAndUser(Long id, User user);

  //Visszaadja a felhasználó cég által már elfogadott számláját, lista elején a még nem elfogadottak, azon belöl dátum szerint rendezi
  List<GroupedFeeItems> findAllByUserAndAcceptedByCompanyTrueOrderByAcceptedByUserAscBeginDateAsc(User user);

  void save(List<GroupedFeeItems> groupedFeeItems);

}