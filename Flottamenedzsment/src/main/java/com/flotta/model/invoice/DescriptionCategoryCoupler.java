package com.flotta.model.invoice;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.flotta.enums.Availability;
import com.flotta.model.BasicEntity;

@Entity
public class DescriptionCategoryCoupler extends BasicEntity {

  private String name;

  private Availability availability;

  @JoinTable(name = "description_category_map")
  @ManyToMany(cascade = CascadeType.ALL)
  private Map<String, Category> descriptionCategoryMap = new HashMap<String, Category>();

  public static final Comparator<DescriptionCategoryCoupler> BY_NAME  = 
      Comparator.comparing(DescriptionCategoryCoupler::getName, String.CASE_INSENSITIVE_ORDER);
  
  public DescriptionCategoryCoupler() {
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
    return "DescriptionCategoryCoupler [name=" + name + ", available=" + availability + "]";
  }
  
  

}
