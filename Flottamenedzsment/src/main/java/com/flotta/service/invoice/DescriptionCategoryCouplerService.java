package com.flotta.service.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.Availability;
import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.repository.invoice.DescriptionCategoryCouplerRepository;

@Service
public class DescriptionCategoryCouplerService {
  
  private DescriptionCategoryCouplerRepository descriptionCategoryCouplerRepository;
  
  @Autowired
  public void setDescriptionCategoryCouplerRepository(DescriptionCategoryCouplerRepository descriptionCategoryCouplerRepository) {
    this.descriptionCategoryCouplerRepository = descriptionCategoryCouplerRepository;
  }

  List<DescriptionCategoryCoupler> findAll() {
    return descriptionCategoryCouplerRepository.findAll();
  }
  
  List<DescriptionCategoryCoupler> findAllByAvailability(Availability availability) {
    return descriptionCategoryCouplerRepository.findAllByAvailability(availability);
  }
  
  Optional<DescriptionCategoryCoupler> findById(long id) {
    return descriptionCategoryCouplerRepository.findById(id);
  }
  
  boolean create(DescriptionCategoryCoupler dcc) {
    Optional<DescriptionCategoryCoupler> optional = descriptionCategoryCouplerRepository.findByName(dcc.getName());
    if(!optional.isPresent()) {
      descriptionCategoryCouplerRepository.save(dcc);
    }
    return !optional.isPresent();
  }

  public boolean update(DescriptionCategoryCoupler coupler, List<String> descriptions, List<Category> categories) {
    if(descriptions == null || categories == null || descriptions.size() != categories.size()) {
      return false;
    }
    for(int i = 0; i < descriptions.size(); i++) {
      coupler.addToDescriptionCategoryMap(descriptions.get(i), categories.get(i));
    }
    descriptionCategoryCouplerRepository.save(coupler);
    return true;
  }
}
