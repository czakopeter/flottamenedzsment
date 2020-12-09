package com.flotta.service.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.Availability;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.invoice.Category;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.repository.invoice.ChargeRatioRepository;
import com.flotta.utility.ExtendedBoolean;

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

  List<ChargeRatioByCategory> findAllByAvailability(Availability availability) {
    return chargeRatioRepository.findAllByAvailability(availability);
  }
  
  Optional<ChargeRatioByCategory> findById(long id) {
    return chargeRatioRepository.findById(id);
  }
  
  ExtendedBoolean create(ChargeRatioByCategory chargeRatio) {
    ExtendedBoolean ex = new ExtendedBoolean(true);
    Optional<ChargeRatioByCategory> optional = chargeRatioRepository.findByName(chargeRatio.getName());
    if(!optional.isPresent()) {
      chargeRatioRepository.save(chargeRatio);
    } else {
      ex.setInvalid();
      ex.addMessage(MessageKey.CHARGE_RATIO_NAME_ALREADY_USED, MessageType.WARNING);
    }
    return ex;
  }

  boolean update(ChargeRatioByCategory chargeRatio, List<Category> categories, List<Integer> ratios) {
    if(categories == null || ratios == null || categories.size() != ratios.size()) {
      return false;
    }
    for(int i = 0; i < ratios.size(); i++) {
      chargeRatio.addToCategoryRatioMap(categories.get(i), ratios.get(i));
    }
    chargeRatioRepository.save(chargeRatio);
    return true;
  }
}
