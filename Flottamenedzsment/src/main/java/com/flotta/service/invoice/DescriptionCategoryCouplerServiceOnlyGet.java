package com.flotta.service.invoice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.invoice.DescriptionCategoryCoupler;
import com.flotta.repository.invoice.DescriptionCategoryCouplerRepository;

@Service
public class DescriptionCategoryCouplerServiceOnlyGet {
  
  private DescriptionCategoryCouplerRepository descriptionCategoryCouplerRepository;
  
  @Autowired
  public void setDescriptionCategoryCouplerRepository(DescriptionCategoryCouplerRepository descriptionCategoryCouplerRepository) {
    this.descriptionCategoryCouplerRepository = descriptionCategoryCouplerRepository;
  }

  public DescriptionCategoryCoupler findById(long id) {
    return descriptionCategoryCouplerRepository.findById(id).orElse(null);
  }
  
}
