package com.flotta.service.invoice;

import java.util.Optional;

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

  void modifyGrossAmountRatio(long id, double user, double company) {
    Optional<FeeItem> feeItemOpt = feeItemRepository.findById(id);
    feeItemOpt.ifPresent(feeItem -> {
      feeItem.setUserGrossAmount(user);
      feeItem.setCompanyGrossAmount(company);
      feeItem.getGroupedFeeItems().setAmountsByFeeItems();
      feeItemRepository.save(feeItem);
    });
  }
}
