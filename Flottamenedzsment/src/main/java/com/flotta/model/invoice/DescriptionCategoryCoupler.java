package com.flotta.model.invoice;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.flotta.model.BasicEntity;

@Entity
public class DescriptionCategoryCoupler extends BasicEntity {

  private String name;

  private boolean available;

  @JoinTable(name = "description_category_map")
  @ManyToMany(cascade = CascadeType.ALL)
  private Map<String, Category> descriptionCategoryMap = new HashMap<String, Category>();

  public DescriptionCategoryCoupler() {
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
      descriptionCategoryMap.put(description, category);
  }

  public List<String> getDescriptions() {
    List<String> descriptions = new LinkedList<>(descriptionCategoryMap.keySet());
    Collections.sort(descriptions);
    return descriptions;
  }
  
  public Category getCategoryByDescription(String description) {
    return descriptionCategoryMap.get(description);
  }

  @Override
  public String toString() {
    return "DescriptionCategoryCoupler [name=" + name + ", available=" + available + "]";
  }
  
  

}
