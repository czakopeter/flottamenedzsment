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

  public FeeItem getById(long id) {
    return feeItemRepository.findById(id).orElse(null);
  }

  public void save(FeeItem feeItem) {
    feeItemRepository.save(feeItem);
  }

  public void modifyGrossAmountRatio(long id, double userAmount, double compAmount) {
    Optional<FeeItem> feeItemOpt = feeItemRepository.findById(id);
    feeItemOpt.ifPresent(feeItem -> {
      feeItem.setUserGrossAmount(userAmount);
      feeItem.setCompanyGrossAmount(compAmount);
      feeItem.getInvoiceByUserAndPhoneNumber().setAmountsByFeeItems();
      feeItemRepository.save(feeItem);
    });
  }
}
