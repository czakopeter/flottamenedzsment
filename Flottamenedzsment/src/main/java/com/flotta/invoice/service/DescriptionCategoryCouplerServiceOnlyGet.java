package com.flotta.invoice.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.invoice.DescriptionCategoryCoupler;
import com.flotta.invoice.repository.DescriptionCategoryCouplerRepository;

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
