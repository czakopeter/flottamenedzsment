package com.flotta.invoice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.invoice.DescriptionCategoryCoupler;


public interface DescriptionCategoryCouplerRepository extends CrudRepository<DescriptionCategoryCoupler, Long>{

  List<DescriptionCategoryCoupler> findAll();
  
  List<DescriptionCategoryCoupler> findAllByAvailableTrue();
  
  List<DescriptionCategoryCoupler> findAllByAvailableFalse();
  
  Optional<DescriptionCategoryCoupler> findByName(String name);

}
