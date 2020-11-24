package com.flotta.service.invoice;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.invoice.Category;
import com.flotta.entity.invoice.DescriptionCategoryCoupler;
import com.flotta.entity.invoice.FeeItem;
import com.flotta.entity.invoice.Invoice;
import com.flotta.exception.invoice.UnknonwFeeItemDescriptionException;
import com.flotta.repository.invoice.DescriptionCategoryCouplerRepository;

@Service
public class DescriptionCategoryCouplerService {
  
  private DescriptionCategoryCouplerRepository descriptionCategoryCouplerRepository;
  
  private Map<Long, List<String> > missingFeeItemDescriptions = new HashMap<Long, List<String> >();
  
  @Autowired
  public void setDescriptionCategoryCouplerRepository(DescriptionCategoryCouplerRepository descriptionCategoryCouplerRepository) {
    this.descriptionCategoryCouplerRepository = descriptionCategoryCouplerRepository;
  }

  public DescriptionCategoryCoupler findById(long id) {
    return descriptionCategoryCouplerRepository.findById(id).orElse(null);
  }

  public Map<Category, List<FeeItem> > partition(Invoice invoice, long templateId) {
    DescriptionCategoryCoupler bpt = descriptionCategoryCouplerRepository.findById(templateId).orElse(null);
    try {
      return bpt.partition(invoice);
    } catch (UnknonwFeeItemDescriptionException e) {
      missingFeeItemDescriptions.put(templateId, e.getUnknownDescriptions());
    }
    return null;
  }
  
  public List<String> getMissingFeeItemDescription(long id) {
    List<String> descriptions = missingFeeItemDescriptions.remove(id);
    Collections.sort(descriptions);
    return descriptions;
  }

  public void updateDescriptionCategoryCoupler(long id, List<String> descriptions, List<Category> categories, boolean available) {
    if(descriptions == null || categories == null) {
      return;
    }
    Optional<DescriptionCategoryCoupler> optional = descriptionCategoryCouplerRepository.findById(id);
    if(optional.isPresent()) {
      DescriptionCategoryCoupler dcc = optional.get();
      Map<String, Category> descriptionCategoryMap = dcc.getDescriptionCategoryMap();
      for(int i = 0; i < descriptions.size(); i++) {
        descriptionCategoryMap.put(descriptions.get(i), categories.get(i));
      }
      dcc.setAvailable(available);
      descriptionCategoryCouplerRepository.save(dcc);
    }
  }

  public List<DescriptionCategoryCoupler> findAll() {
    return descriptionCategoryCouplerRepository.findAll();
  }

  public List<String> findAllInvoiceDescription() {
    Set<String> descriptions = new HashSet<>();
    for(DescriptionCategoryCoupler coupler : descriptionCategoryCouplerRepository.findAll()) {
      descriptions.addAll(coupler.getDescriptionCategoryMap().keySet());
    }
    List<String> result = new LinkedList<>(descriptions);
    Collections.sort(result);
    return result;
  }

  public boolean add(DescriptionCategoryCoupler dcc) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean descriptionCategoryCoupler(DescriptionCategoryCoupler dcc) {
    Optional<DescriptionCategoryCoupler> optional = descriptionCategoryCouplerRepository.findByName(dcc.getName());
    if(!optional.isPresent()) {
      descriptionCategoryCouplerRepository.save(dcc);
    }
    return !optional.isPresent();
  }
  
}
