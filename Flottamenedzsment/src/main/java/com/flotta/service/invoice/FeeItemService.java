package com.flotta.service.invoice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.invoice.FeeItem;
import com.flotta.repository.invoice.FeeItemRepository;

@Service
public class FeeItemService {

  private FeeItemRepository feeItemRepository;

  @Autowired
  public void setFeeItemRepository(FeeItemRepository feeItemRepository) {
    this.feeItemRepository = feeItemRepository;
  }
  
  public List<FeeItem> findAllByUserId(long userId) {
    return feeItemRepository.findAllByUserId(userId);
  }

  public void save(List<FeeItem> fees) {
    feeItemRepository.saveAll(fees);
    
  }

  public FeeItem getById(long id) {
    return feeItemRepository.findById(id).orElse(null);
  }

  public void save(FeeItem feeItem) {
    feeItemRepository.save(feeItem);
  }
}
