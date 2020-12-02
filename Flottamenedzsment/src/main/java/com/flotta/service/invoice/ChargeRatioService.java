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

  public boolean addChargeRatio(ChargeRatioByCategory chargeRatio) {
    Optional<ChargeRatioByCategory> optional = chargeRatioRepository.findByName(chargeRatio.getName());
    
    if(!optional.isPresent()) {
      chargeRatio.setAvailable(true);
      chargeRatioRepository.save(chargeRatio);
    }
    return !optional.isPresent();
  }

  public List<ChargeRatioByCategory> findAll() {
    return chargeRatioRepository.findAll();
  }

  public Optional<ChargeRatioByCategory> findChargeRatioById(long id) {
    return chargeRatioRepository.findById(id);
  }

  public boolean updateChargeRatio(long id, List<Category> categories, List<Integer> ratios) {
    Optional<ChargeRatioByCategory> optional = chargeRatioRepository.findById(id);
    if(optional.isPresent() && categories.size() == ratios.size()) {
      ChargeRatioByCategory entity = optional.get();
      for(int i = 0; i < ratios.size(); i++) {
        entity.add(categories.get(i), ratios.get(i));
      }
      chargeRatioRepository.save(entity);
    }
    return optional.isPresent();
  }

}
