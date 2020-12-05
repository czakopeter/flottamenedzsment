package com.flotta.model.invoice;

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.flotta.model.BasicEntity;

@Entity
@Table(name = "categories")
public class Category extends BasicEntity implements Comparable<Category> {
  
  private String name;
  
  public static final Comparator<Category> BY_NAME  = 
      Comparator.comparing(Category::getName, String.CASE_INSENSITIVE_ORDER);
  
  public Category() {}
  
  public Category(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int compareTo(Category o) {
    return BY_NAME.compare(this, o);
  }
  
}
