package com.flotta.repository.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.invoice.ChargeRatioByCategory;

public interface ChargeRatioRepository extends CrudRepository<ChargeRatioByCategory, Long> {

  Optional<ChargeRatioByCategory> findByName(String name);
  
  List<ChargeRatioByCategory> findAll();

}
