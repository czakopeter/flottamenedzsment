package com.flotta.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.invoice.DescriptionCategoryCoupler;


public interface DescriptionCategoryCouplerRepository extends CrudRepository<DescriptionCategoryCoupler, Long>{

  List<DescriptionCategoryCoupler> findAll();
  
  List<DescriptionCategoryCoupler> findAllByAvailableTrue();
  
  List<DescriptionCategoryCoupler> findAllByAvailableFalse();
  
  DescriptionCategoryCoupler findByName(String name);

}
