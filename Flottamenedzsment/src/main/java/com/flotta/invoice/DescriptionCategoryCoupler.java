package com.flotta.invoice;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.flotta.invoice.exception.UnknonwFeeItemDescriptionException;

@Entity
public class DescriptionCategoryCoupler {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  private boolean available;

  @JoinTable(name = "description_category_map")
  @ManyToMany(cascade = CascadeType.ALL)
  private Map<String, Category> descriptionCategoryMap = new HashMap<String, Category>();

  public DescriptionCategoryCoupler() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public Map<String, Category> getDescriptionCategoryMap() {
    return descriptionCategoryMap;
  }

  public void setDescriptionCategoryMap(Map<String, Category> descriptionCategoryMap) {
    this.descriptionCategoryMap = descriptionCategoryMap;
  }

  public void addToDescriptionCategoryMap(String description, Category category) {
    Category cat = descriptionCategoryMap.get(description);
    if (cat == null) {
      descriptionCategoryMap.put(description, category);
    }
  }

  public Map<Category, List<FeeItem> > partition(Invoice invoice) throws UnknonwFeeItemDescriptionException {
    Map<Category, List<FeeItem>> result = new HashMap<>();
    Set<String> unknownFreeItemDesc = new HashSet<>();
    for(InvoiceByUserAndPhoneNumber part : invoice.getInvoicePart()) {
      for (FeeItem fee : part.getFees()) {
        Category category = descriptionCategoryMap.get(fee.getDescription());
        if (category == null) {
          unknownFreeItemDesc.add(fee.getDescription());
        } else {
          fee.setCategory(category);
        }
        if (unknownFreeItemDesc.size() != 0) {
          List<FeeItem> value = result.get(category);
          if (value == null) {
            value = new LinkedList<FeeItem>();
          }
          value.add(fee);
          result.put(category, value);
        }
      }
    }
    if(!unknownFreeItemDesc.isEmpty()) {
      throw new UnknonwFeeItemDescriptionException(unknownFreeItemDesc);
    }
    return result;
  }
  
  public List<String> getSortedDescriptions() {
    List<String> descriptions = new LinkedList<>(descriptionCategoryMap.keySet());
    Collections.sort(descriptions);
    return descriptions;
  }
  
  public Category getCategoryByDescription(String description) {
    return descriptionCategoryMap.get(description);
  }

}
