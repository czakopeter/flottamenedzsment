package com.flotta.service.invoice;

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

  public FeeItem getById(long id) {
    return feeItemRepository.findById(id).orElse(null);
  }

  public void save(FeeItem feeItem) {
    feeItemRepository.save(feeItem);
  }
}
