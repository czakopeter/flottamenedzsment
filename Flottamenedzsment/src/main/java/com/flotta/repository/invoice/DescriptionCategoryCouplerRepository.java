package com.flotta.repository.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.enums.Availability;
import com.flotta.model.invoice.DescriptionCategoryCoupler;


public interface DescriptionCategoryCouplerRepository extends CrudRepository<DescriptionCategoryCoupler, Long>{

  List<DescriptionCategoryCoupler> findAll();
  
  Optional<DescriptionCategoryCoupler> findByName(String name);

  List<DescriptionCategoryCoupler> findAllByAvailability(Availability availability);

}
