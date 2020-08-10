package com.flotta.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.invoice.ChargeRatioByCategory;

public interface ChargeRatioRepository extends CrudRepository<ChargeRatioByCategory, Long> {

  ChargeRatioByCategory findById(long id);
  
  ChargeRatioByCategory findByName(String name);
  
  List<ChargeRatioByCategory> findAll();

}
