package com.flotta.model.invoice;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;

import com.flotta.model.BasicEntity;

@Entity
public class ChargeRatioByCategory extends BasicEntity {
  
  private String name;
  
  private boolean available;
  
  @ElementCollection
  @JoinTable(name = "category_ratio_map")
  private Map<Category, Integer> categoryRatioMap = new HashMap<>();
  
  public ChargeRatioByCategory() {
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

  public void add(Category category, int ratio) {
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
  
  public List<Category> getOrderedCategories() {
    List<Category> result = new LinkedList<Category>(categoryRatioMap.keySet());
    Collections.sort(result);
    return result;
  }

  public int getRatioByCategory(Category category) {
    return categoryRatioMap.get(category);
  }
  
}
