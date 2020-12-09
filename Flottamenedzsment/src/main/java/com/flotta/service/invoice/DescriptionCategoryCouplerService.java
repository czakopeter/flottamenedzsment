package com.flotta.service.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.Availability;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.repository.invoice.DescriptionCategoryCouplerRepository;
import com.flotta.utility.ExtendedBoolean;

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
  
  ExtendedBoolean create(DescriptionCategoryCoupler dcc) {
    ExtendedBoolean eb = new ExtendedBoolean(true);
    Optional<DescriptionCategoryCoupler> optional = descriptionCategoryCouplerRepository.findByName(dcc.getName());
    if(!optional.isPresent()) {
      descriptionCategoryCouplerRepository.save(dcc);
    } else {
      eb.setInvalid();
      eb.addMessage(MessageKey.COUPLER_NAME_ALREADY_USED, MessageType.WARNING);
    }
    return eb;
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
