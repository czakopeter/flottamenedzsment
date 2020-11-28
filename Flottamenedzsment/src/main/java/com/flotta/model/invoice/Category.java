package com.flotta.model.invoice;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.flotta.model.BasicEntity;

@Entity
@Table(name = "categories")
public class Category extends BasicEntity implements Comparable<Category> {
  
  private String name;
  
  BasicFeeItem fee = new FeeItem();
  
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
    return name.compareToIgnoreCase(o.name);
  }
  
}
