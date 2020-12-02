package com.flotta.service.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  boolean update(long id, List<String> descriptions, List<Category> categories, boolean available) {
    if(descriptions == null || categories == null) {
      return false;
    }
    Optional<DescriptionCategoryCoupler> dccOpt = descriptionCategoryCouplerRepository.findById(id);
    dccOpt.ifPresent(dcc ->{
      for(int i = 0; i < descriptions.size(); i++) {
        dcc.addToDescriptionCategoryMap(descriptions.get(i), categories.get(i));
      }
      dcc.setAvailable(available);
      descriptionCategoryCouplerRepository.save(dcc);
    });
    return dccOpt.isPresent();
  }
}
