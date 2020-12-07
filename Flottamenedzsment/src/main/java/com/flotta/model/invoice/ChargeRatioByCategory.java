package com.flotta.model.invoice;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinTable;

import com.flotta.enums.Availability;
import com.flotta.model.BasicEntity;

@Entity
public class ChargeRatioByCategory extends BasicEntity {
  
  private String name;
  
  private Availability availability;
  
  @ElementCollection
  @JoinTable(name = "category_ratio_map")
  private Map<Category, Integer> categoryRatioMap = new HashMap<>();
  
  public static final Comparator<ChargeRatioByCategory> BY_NAME  = 
      Comparator.comparing(ChargeRatioByCategory::getName, String.CASE_INSENSITIVE_ORDER);
  
  public ChargeRatioByCategory() {
    availability = Availability.NONE;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Availability getAvailability() {
    return availability;
  }

  public void setAvailability(Availability availability) {
    this.availability = availability;
  }

  public void addToCategoryRatioMap(Category category, int ratio) {
    if(validRatio(ratio)) {
      categoryRatioMap.put(category, ratio);
    }
  }

  public Map<Category, Integer> getCategoryRatioMap() {
    return categoryRatioMap;
  }

  public void setCategoryRatioMap(Map<Category, Integer> categoryRatioMap) {
    this.categoryRatioMap = categoryRatioMap;
  }

  private boolean validRatio(int ratio) {
    return ratio >= 0 && ratio <= 100 && ratio % 5 == 0;
  }
  
  public List<Category> getCategories() {
    List<Category> result = new LinkedList<Category>(categoryRatioMap.keySet());
    Collections.sort(result);
    return result;
  }

  public int getRatioByCategory(Category category) {
    return categoryRatioMap.get(category);
  }

  @Override
  public String toString() {
    return "ChargeRatioByCategory [name=" + name + ", available=" + availability + "]";
  }
  
}
