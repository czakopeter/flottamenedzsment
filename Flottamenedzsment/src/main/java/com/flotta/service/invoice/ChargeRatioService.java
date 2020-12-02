package com.flotta.service.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.repository.invoice.ChargeRatioRepository;

@Service
public class ChargeRatioService {

  private ChargeRatioRepository chargeRatioRepository;

  @Autowired
  public void setChargeRatioRepository(ChargeRatioRepository chargeRatioRepository) {
    this.chargeRatioRepository = chargeRatioRepository;
  }
  
  public ChargeRatioService() {}

  List<ChargeRatioByCategory> findAll() {
    return chargeRatioRepository.findAll();
  }

  Optional<ChargeRatioByCategory> findById(long id) {
    return chargeRatioRepository.findById(id);
  }
  
  boolean create(ChargeRatioByCategory chargeRatio) {
    Optional<ChargeRatioByCategory> optional = chargeRatioRepository.findByName(chargeRatio.getName());
    if(!optional.isPresent()) {
      chargeRatio.setAvailable(true);
      chargeRatioRepository.save(chargeRatio);
    }
    return !optional.isPresent();
  }

  boolean update(long id, List<Category> categories, List<Integer> ratios) {
    if(categories.size() != ratios.size()) {
      return false;
    }
    
    Optional<ChargeRatioByCategory> chargeRatioOpt = chargeRatioRepository.findById(id);
    chargeRatioOpt.ifPresent(chargeRatio -> {
      for(int i = 0; i < ratios.size(); i++) {
        chargeRatio.addToCategoryRatioMap(categories.get(i), ratios.get(i));
      }
      chargeRatioRepository.save(chargeRatio);
    });
    return chargeRatioOpt.isPresent();
  }

}
